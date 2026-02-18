package com.web3social.authservice.controller;

import com.web3social.authservice.dto.AuthRequest;
import com.web3social.authservice.dto.AuthResponse;
import com.web3social.authservice.dto.UserDto;
import com.web3social.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/nonce/{walletAddress}")
    public ResponseEntity<String> getNonce(@PathVariable String walletAddress) {
        String nonce = authService.generateNonce(walletAddress);
        return ResponseEntity.ok(nonce);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody @Valid AuthRequest request) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{walletAddress}")
    public ResponseEntity<UserDto> getUser(@PathVariable String walletAddress) {
        UserDto user = authService.getUserByWalletAddress(walletAddress);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/user/{walletAddress}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable String walletAddress,
            @RequestBody UserDto userDto) {
        UserDto updated = authService.updateUser(walletAddress, userDto);
        return ResponseEntity.ok(updated);
    }
}