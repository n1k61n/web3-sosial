package com.web3sosial.userservice.repository;

import com.web3sosial.userservice.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, String> {
    boolean existsByFollowerAddressAndFollowingAddress(String followerAddress, String followingAddress);
    Optional<Follow> findByFollowerAddressAndFollowingAddress(String followerAddress, String followingAddress);
    List<Follow> findByFollowerAddress(String followerAddress);
    List<Follow> findByFollowingAddress(String followingAddress);
}