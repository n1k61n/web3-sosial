package com.web3sosial.postservice.service;

import com.web3sosial.postservice.dto.CommentDto;
import com.web3sosial.postservice.dto.CreateCommentRequest;
import com.web3sosial.postservice.dto.CreatePostRequest;
import com.web3sosial.postservice.dto.PostDto;
import com.web3sosial.postservice.entity.Comment;
import com.web3sosial.postservice.entity.Like;
import com.web3sosial.postservice.entity.Post;
import com.web3sosial.postservice.repository.CommentRepository;
import com.web3sosial.postservice.repository.LikeRepository;
import com.web3sosial.postservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public PostDto createPost(CreatePostRequest request) {
        Post post = Post.builder()
                .walletAddress(request.getWalletAddress())
                .content(request.getContent())
                .mediaUrl(request.getMediaUrl())
                .postType(request.getPostType())
                .likesCount(0)
                .commentsCount(0)
                .repostsCount(0)
                .isDeleted(false)
                .build();

        return mapToDto(postRepository.save(post));
    }

    public PostDto getPost(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return mapToDto(post);
    }

    public List<PostDto> getAllPosts() {
        return postRepository.findByIsDeletedFalseOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<PostDto> getUserPosts(String walletAddress) {
        return postRepository.findByWalletAddressAndIsDeletedFalseOrderByCreatedAtDesc(walletAddress)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void deletePost(String postId, String walletAddress) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getWalletAddress().equals(walletAddress)) {
            throw new RuntimeException("Unauthorized");
        }

        post.setIsDeleted(true);
        postRepository.save(post);
    }

    public CommentDto addComment(String postId, CreateCommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = Comment.builder()
                .postId(postId)
                .walletAddress(request.getWalletAddress())
                .content(request.getContent())
                .likesCount(0)
                .build();

        Comment saved = commentRepository.save(comment);

        post.setCommentsCount(post.getCommentsCount() + 1);
        postRepository.save(post);

        return mapToCommentDto(saved);
    }

    public List<CommentDto> getComments(String postId) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId)
                .stream()
                .map(this::mapToCommentDto)
                .collect(Collectors.toList());
    }

    public void likePost(String postId, String walletAddress) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (likeRepository.existsByPostIdAndWalletAddress(postId, walletAddress)) {
            throw new RuntimeException("Already liked");
        }

        Like like = Like.builder()
                .postId(postId)
                .walletAddress(walletAddress)
                .build();

        likeRepository.save(like);

        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);
    }

    public void unlikePost(String postId, String walletAddress) {
        Like like = likeRepository.findByPostIdAndWalletAddress(postId, walletAddress)
                .orElseThrow(() -> new RuntimeException("Not liked"));

        likeRepository.delete(like);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setLikesCount(Math.max(0, post.getLikesCount() - 1));
        postRepository.save(post);
    }

    private PostDto mapToDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .walletAddress(post.getWalletAddress())
                .content(post.getContent())
                .ipfsHash(post.getIpfsHash())
                .mediaUrl(post.getMediaUrl())
                .postType(post.getPostType())
                .likesCount(post.getLikesCount())
                .commentsCount(post.getCommentsCount())
                .repostsCount(post.getRepostsCount())
                .createdAt(post.getCreatedAt())
                .build();
    }

    private CommentDto mapToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .walletAddress(comment.getWalletAddress())
                .content(comment.getContent())
                .likesCount(comment.getLikesCount())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}