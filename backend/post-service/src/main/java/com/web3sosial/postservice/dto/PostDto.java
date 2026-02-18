package com.web3sosial.postservice.dto;

import com.web3sosial.postservice.entity.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
    private String id;
    private String walletAddress;
    private String content;
    private String ipfsHash;
    private String mediaUrl;
    private PostType postType;
    private Integer likesCount;
    private Integer commentsCount;
    private Integer repostsCount;
    private LocalDateTime createdAt;
}