package com.web3sosial.postservice.repository;

import com.web3sosial.postservice.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, String> {
    boolean existsByPostIdAndWalletAddress(String postId, String walletAddress);
    Optional<Like> findByPostIdAndWalletAddress(String postId, String walletAddress);
    Integer countByPostId(String postId);
}