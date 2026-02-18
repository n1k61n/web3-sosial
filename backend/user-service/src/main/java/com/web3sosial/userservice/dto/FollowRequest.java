package com.web3sosial.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowRequest {
    @NotBlank
    private String followerAddress;
    @NotBlank
    private String followingAddress;
}