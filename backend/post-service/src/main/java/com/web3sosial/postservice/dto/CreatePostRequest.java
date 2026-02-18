package com.web3sosial.postservice.dto;

import com.web3sosial.postservice.entity.PostType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {
    @NotBlank
    private String walletAddress;
    @NotBlank
    private String content;
    private String mediaUrl;
    private PostType postType = PostType.TEXT;
}