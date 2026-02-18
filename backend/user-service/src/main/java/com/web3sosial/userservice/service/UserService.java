package com.web3sosial.userservice.service;

import com.web3sosial.userservice.dto.FollowRequest;
import com.web3sosial.userservice.dto.NotificationEvent;
import com.web3sosial.userservice.dto.UpdateProfileRequest;
import com.web3sosial.userservice.dto.UserProfileDto;
import com.web3sosial.userservice.entity.Follow;
import com.web3sosial.userservice.entity.UserProfile;
import com.web3sosial.userservice.repository.FollowRepository;
import com.web3sosial.userservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserProfileRepository userProfileRepository;
    private final FollowRepository followRepository;
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;


    public UserProfileDto getProfile(String walletAddress) {
        UserProfile profile = userProfileRepository.findByWalletAddress(walletAddress)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDto(profile);
    }

    public UserProfileDto createProfile(String walletAddress) {
        if (userProfileRepository.existsByWalletAddress(walletAddress)) {
            return getProfile(walletAddress);
        }

        UserProfile profile = UserProfile.builder()
                .walletAddress(walletAddress)
                .followersCount(0)
                .followingCount(0)
                .postsCount(0)
                .build();

        return mapToDto(userProfileRepository.save(profile));
    }

    public UserProfileDto updateProfile(String walletAddress, UpdateProfileRequest request) {
        UserProfile profile = userProfileRepository.findByWalletAddress(walletAddress)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getUsername() != null) {
            if (userProfileRepository.existsByUsername(request.getUsername())) {
                throw new RuntimeException("Username already taken");
            }
            profile.setUsername(request.getUsername());
        }

        if (request.getBio() != null) profile.setBio(request.getBio());
        if (request.getAvatarUrl() != null) profile.setAvatarUrl(request.getAvatarUrl());
        if (request.getCoverUrl() != null) profile.setCoverUrl(request.getCoverUrl());

        return mapToDto(userProfileRepository.save(profile));
    }

    public void follow(FollowRequest request) {
        if (request.getFollowerAddress().equals(request.getFollowingAddress())) {
            throw new RuntimeException("Cannot follow yourself");
        }

        if (followRepository.existsByFollowerAddressAndFollowingAddress(
                request.getFollowerAddress(), request.getFollowingAddress())) {
            throw new RuntimeException("Already following");
        }

        Follow follow = Follow.builder()
                .followerAddress(request.getFollowerAddress())
                .followingAddress(request.getFollowingAddress())
                .build();

        followRepository.save(follow);

        userProfileRepository.findByWalletAddress(request.getFollowerAddress())
                .ifPresent(p -> {
                    p.setFollowingCount(p.getFollowingCount() + 1);
                    userProfileRepository.save(p);
                });

        userProfileRepository.findByWalletAddress(request.getFollowingAddress())
                .ifPresent(p -> {
                    p.setFollowersCount(p.getFollowersCount() + 1);
                    userProfileRepository.save(p);
                });

        // Kafka event göndər
        kafkaTemplate.send("notifications", NotificationEvent.builder()
                .recipientAddress(request.getFollowingAddress())
                .senderAddress(request.getFollowerAddress())
                .type("FOLLOW")
                .referenceId(request.getFollowerAddress())
                .message(request.getFollowerAddress() + " sizi izləməyə başladı")
                .build());
    }

    public void unfollow(FollowRequest request) {
        Follow follow = followRepository.findByFollowerAddressAndFollowingAddress(
                        request.getFollowerAddress(), request.getFollowingAddress())
                .orElseThrow(() -> new RuntimeException("Not following"));

        followRepository.delete(follow);

        userProfileRepository.findByWalletAddress(request.getFollowerAddress())
                .ifPresent(p -> {
                    p.setFollowingCount(Math.max(0, p.getFollowingCount() - 1));
                    userProfileRepository.save(p);
                });

        userProfileRepository.findByWalletAddress(request.getFollowingAddress())
                .ifPresent(p -> {
                    p.setFollowersCount(Math.max(0, p.getFollowersCount() - 1));
                    userProfileRepository.save(p);
                });
    }

    public List<UserProfileDto> getFollowers(String walletAddress) {
        return followRepository.findByFollowingAddress(walletAddress)
                .stream()
                .map(f -> userProfileRepository.findByWalletAddress(f.getFollowerAddress())
                        .map(this::mapToDto)
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<UserProfileDto> getFollowing(String walletAddress) {
        return followRepository.findByFollowerAddress(walletAddress)
                .stream()
                .map(f -> userProfileRepository.findByWalletAddress(f.getFollowingAddress())
                        .map(this::mapToDto)
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private UserProfileDto mapToDto(UserProfile profile) {
        return UserProfileDto.builder()
                .id(profile.getId())
                .walletAddress(profile.getWalletAddress())
                .username(profile.getUsername())
                .bio(profile.getBio())
                .avatarUrl(profile.getAvatarUrl())
                .coverUrl(profile.getCoverUrl())
                .followersCount(profile.getFollowersCount())
                .followingCount(profile.getFollowingCount())
                .postsCount(profile.getPostsCount())
                .createdAt(profile.getCreatedAt())
                .build();
    }
}