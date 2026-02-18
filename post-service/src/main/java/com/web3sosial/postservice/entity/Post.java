package com.web3sosial.postservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String walletAddress;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private String ipfsHash;

    @Column
    private String mediaUrl;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Column
    private Integer likesCount = 0;

    @Column
    private Integer commentsCount = 0;

    @Column
    private Integer repostsCount = 0;

    @Column
    private Boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}