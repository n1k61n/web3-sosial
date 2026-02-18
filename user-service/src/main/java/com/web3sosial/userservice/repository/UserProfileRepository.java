package com.web3sosial.userservice.repository;

import com.web3sosial.userservice.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    Optional<UserProfile> findByWalletAddress(String walletAddress);
    Optional<UserProfile> findByUsername(String username);
    boolean existsByWalletAddress(String walletAddress);
    boolean existsByUsername(String username);
}