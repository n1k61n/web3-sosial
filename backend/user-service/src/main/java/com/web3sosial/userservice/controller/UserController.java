package com.web3sosial.userservice.controller;

import com.web3sosial.userservice.dto.FollowRequest;
import com.web3sosial.userservice.dto.UpdateProfileRequest;
import com.web3sosial.userservice.dto.UserProfileDto;
import com.web3sosial.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{walletAddress}")
    public ResponseEntity<UserProfileDto> getProfile(@PathVariable String walletAddress) {
        return ResponseEntity.ok(userService.getProfile(walletAddress));
    }

    @PostMapping("/{walletAddress}")
    public ResponseEntity<UserProfileDto> createProfile(@PathVariable String walletAddress) {
        return ResponseEntity.ok(userService.createProfile(walletAddress));
    }

    @PutMapping("/{walletAddress}")
    public ResponseEntity<UserProfileDto> updateProfile(
            @PathVariable String walletAddress,
            @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(walletAddress, request));
    }

    @PostMapping("/follow")
    public ResponseEntity<Void> follow(@RequestBody @Valid FollowRequest request) {
        userService.follow(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unfollow")
    public ResponseEntity<Void> unfollow(@RequestBody @Valid FollowRequest request) {
        userService.unfollow(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{walletAddress}/followers")
    public ResponseEntity<List<UserProfileDto>> getFollowers(@PathVariable String walletAddress) {
        return ResponseEntity.ok(userService.getFollowers(walletAddress));
    }

    @GetMapping("/{walletAddress}/following")
    public ResponseEntity<List<UserProfileDto>> getFollowing(@PathVariable String walletAddress) {
        return ResponseEntity.ok(userService.getFollowing(walletAddress));
    }
}