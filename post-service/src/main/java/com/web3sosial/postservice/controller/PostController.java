package com.web3sosial.postservice.controller;

import com.web3sosial.postservice.dto.CommentDto;
import com.web3sosial.postservice.dto.CreateCommentRequest;
import com.web3sosial.postservice.dto.CreatePostRequest;
import com.web3sosial.postservice.dto.PostDto;
import com.web3sosial.postservice.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody @Valid CreatePostRequest request) {
        return ResponseEntity.ok(postService.createPost(request));
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable String postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @GetMapping("/user/{walletAddress}")
    public ResponseEntity<List<PostDto>> getUserPosts(@PathVariable String walletAddress) {
        return ResponseEntity.ok(postService.getUserPosts(walletAddress));
    }

    @DeleteMapping("/{postId}/{walletAddress}")
    public ResponseEntity<Void> deletePost(
            @PathVariable String postId,
            @PathVariable String walletAddress) {
        postService.deletePost(postId, walletAddress);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable String postId,
            @RequestBody @Valid CreateCommentRequest request) {
        return ResponseEntity.ok(postService.addComment(postId, request));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable String postId) {
        return ResponseEntity.ok(postService.getComments(postId));
    }

    @PostMapping("/{postId}/like/{walletAddress}")
    public ResponseEntity<Void> likePost(
            @PathVariable String postId,
            @PathVariable String walletAddress) {
        postService.likePost(postId, walletAddress);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/like/{walletAddress}")
    public ResponseEntity<Void> unlikePost(
            @PathVariable String postId,
            @PathVariable String walletAddress) {
        postService.unlikePost(postId, walletAddress);
        return ResponseEntity.ok().build();
    }
}