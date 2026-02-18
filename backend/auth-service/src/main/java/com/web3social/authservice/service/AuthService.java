package com.web3social.authservice.service;

import com.web3social.authservice.dto.AuthRequest;
import com.web3social.authservice.dto.AuthResponse;
import com.web3social.authservice.dto.UserDto;
import com.web3social.authservice.entity.Role;
import com.web3social.authservice.entity.User;
import com.web3social.authservice.repository.UserRepository;
import com.web3social.authservice.security.JwtService;
import com.web3social.authservice.security.Web3SignatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final Web3SignatureService web3SignatureService;

    public String generateNonce(String walletAddress) {
        return "Sign this message to authenticate: " +
                walletAddress + " - " +
                System.currentTimeMillis();
    }

    public AuthResponse authenticate(AuthRequest request) {
        boolean isValid = web3SignatureService.verifySignature(
                request.getWalletAddress(),
                request.getMessage(),
                request.getSignature()
        );

        if (!isValid) {
            throw new RuntimeException("Invalid signature");
        }

        User user = userRepository.findByWalletAddress(request.getWalletAddress())
                .orElseGet(() -> registerNewUser(request.getWalletAddress()));

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .walletAddress(user.getWalletAddress())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    private User registerNewUser(String walletAddress) {
        User newUser = User.builder()
                .walletAddress(walletAddress)
                .role(Role.USER)
                .build();
        return userRepository.save(newUser);
    }

    public UserDto getUserByWalletAddress(String walletAddress) {
        User user = userRepository.findByWalletAddress(walletAddress)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserDto.builder()
                .id(user.getId())
                .walletAddress(user.getWalletAddress())
                .username(user.getUsername())
                .bio(user.getBio())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public UserDto updateUser(String walletAddress, UserDto userDto) {
        User user = userRepository.findByWalletAddress(walletAddress)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userDto.getUsername());
        user.setBio(userDto.getBio());
        user.setAvatarUrl(userDto.getAvatarUrl());

        User saved = userRepository.save(user);

        return UserDto.builder()
                .id(saved.getId())
                .walletAddress(saved.getWalletAddress())
                .username(saved.getUsername())
                .bio(saved.getBio())
                .avatarUrl(saved.getAvatarUrl())
                .role(saved.getRole().name())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}