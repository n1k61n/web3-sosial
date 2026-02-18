package com.web3social.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String id;
    private String walletAddress;
    private String username;
    private String bio;
    private String avatarUrl;
    private String role;
    private LocalDateTime createdAt;
}