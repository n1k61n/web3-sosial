package com.web3sosial.blockchain.controller;

import com.web3sosial.blockchain.dto.*;
import com.web3sosial.blockchain.service.SmartContractService;
import com.web3sosial.blockchain.service.Web3jService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blockchain")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BlockchainController {

    private final SmartContractService smartContractService;
    private final Web3jService web3jService;

    /**
     * Verify Ethereum signature
     */
    @PostMapping("/verify-signature")
    public ResponseEntity<SignatureVerificationResponse> verifySignature(
            @RequestBody SignatureVerificationRequest request) {
        
        boolean isValid = smartContractService.verifySignature(
            request.getMessage(),
            request.getSignature(),
            request.getAddress()
        );
        
        return ResponseEntity.ok(SignatureVerificationResponse.builder()
            .valid(isValid)
            .message(isValid ? "Signature verified" : "Invalid signature")
            .build());
    }

    /**
     * Create post on IPFS/blockchain
     */
    @PostMapping("/posts")
    public ResponseEntity<BlockchainResponse> createPost(
            @RequestParam String wallet,
            @RequestBody PostRequest request) {
        
        BlockchainResponse response = smartContractService.createPost(wallet, request.getContent());
        return ResponseEntity.ok(response);
    }

    /**
     * Create profile on IPFS/blockchain
     */
    @PostMapping("/profiles")
    public ResponseEntity<BlockchainResponse> createProfile(
            @RequestParam String wallet,
            @RequestBody ProfileRequest request) {
        
        BlockchainResponse response = smartContractService.createProfile(
            wallet, request.getUsername(), request.getBio());
        return ResponseEntity.ok(response);
    }

    /**
     * Process like and distribute rewards
     */
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<BlockchainResponse> likePost(
            @PathVariable Long postId,
            @RequestParam String wallet,
            @RequestParam String postAuthor) {
        
        BlockchainResponse response = smartContractService.processLike(wallet, postAuthor, postId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get token balance
     */
    @GetMapping("/tokens/balance/{wallet}")
    public ResponseEntity<TokenBalanceResponse> getTokenBalance(
            @PathVariable String wallet) {
        
        TokenBalanceResponse response = smartContractService.getTokenBalance(wallet);
        return ResponseEntity.ok(response);
    }

    /**
     * Get reward summary
     */
    @GetMapping("/rewards/{wallet}")
    public ResponseEntity<RewardSummaryResponse> getRewardSummary(
            @PathVariable String wallet) {
        
        RewardSummaryResponse response = smartContractService.getRewardSummary(wallet);
        return ResponseEntity.ok(response);
    }

    /**
     * Get ETH balance
     */
    @GetMapping("/balance/{wallet}")
    public ResponseEntity<BalanceResponse> getEthBalance(
            @PathVariable String wallet) {
        try {
            Double balance = web3jService.getEthBalance(wallet);
            return ResponseEntity.ok(BalanceResponse.builder()
                .wallet(wallet)
                .balance(balance.toString())
                .currency("ETH")
                .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get current block number
     */
    @GetMapping("/block-number")
    public ResponseEntity<BlockNumberResponse> getBlockNumber() {
        try {
            Long blockNumber = web3jService.getCurrentBlockNumber();
            return ResponseEntity.ok(BlockNumberResponse.builder()
                .blockNumber(blockNumber)
                .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Check if user exists on blockchain
     */
    @GetMapping("/users/{wallet}/exists")
    public ResponseEntity<UserExistsResponse> userExists(
            @PathVariable String wallet) {
        
        // TODO: Implement smart contract query
        return ResponseEntity.ok(UserExistsResponse.builder()
            .wallet(wallet)
            .exists(false)
            .message("Blockchain integration in progress")
            .build());
    }

    // Request/Response DTOs
    
    @Data
    public static class SignatureVerificationRequest {
        private String message;
        private String signature;
        private String address;
    }
    
    @Data
    @Builder
    public static class SignatureVerificationResponse {
        private boolean valid;
        private String message;
    }
    
    @Data
    public static class PostRequest {
        private String content;
    }
    
    @Data
    public static class ProfileRequest {
        private String username;
        private String bio;
    }
    
    @Data
    @Builder
    public static class BalanceResponse {
        private String wallet;
        private String balance;
        private String currency;
    }
    
    @Data
    @Builder
    public static class BlockNumberResponse {
        private Long blockNumber;
    }
    
    @Data
    @Builder
    public static class UserExistsResponse {
        private String wallet;
        private boolean exists;
        private String message;
    }
}
