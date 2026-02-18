package com.web3sosial.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private String id;
    private String postId;
    private String walletAddress;
    private String content;
    private Integer likesCount;
    private LocalDateTime createdAt;
}