package com.web3sosial.blockchain.service;

import com.web3sosial.blockchain.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class SmartContractService {

    private final Web3jService web3jService;
    private final IpfsService ipfsService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    
    @Value("${blockchain.private-key:}")
    private String privateKey;
    
    @Value("${blockchain.contract.token-address:}")
    private String tokenContractAddress;
    
    @Value("${blockchain.contract.platform-address:}")
    private String platformContractAddress;

    public SmartContractService(
            Web3jService web3jService,
            IpfsService ipfsService,
            KafkaTemplate<String, String> kafkaTemplate) {
        this.web3jService = web3jService;
        this.ipfsService = ipfsService;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Verify Ethereum signature for authentication
     */
    public boolean verifySignature(String message, String signature, String address) {
        try {
            byte[] messageBytes = message.getBytes();
            byte[] signatureBytes = Sign.stringToSignature(signature);
            
            int v = signatureBytes[64];
            if (v < 27) {
                v += 27;
            }
            
            byte[] r = new byte[32];
            byte[] s = new byte[32];
            System.arraycopy(signatureBytes, 0, r, 0, 32);
            System.arraycopy(signatureBytes, 32, s, 0, 32);
            
            Sign.SignatureData signatureData = new Sign.SignatureData((byte) v, r, s);
            
            BigInteger recoveredKey = Sign.signedMessageToKey(messageBytes, signatureData);
            String recoveredAddress = Keys.getAddress(ECKeyPair.recoverAddress(recoveredKey));
            
            return recoveredAddress.equalsIgnoreCase(address);
        } catch (SignatureException e) {
            return false;
        }
    }

    /**
     * Store post content on IPFS and emit blockchain event
     */
    public BlockchainResponse createPost(String walletAddress, String content) {
        try {
            // Upload content to IPFS
            Map<String, Object> postData = new HashMap<>();
            postData.put("author", walletAddress);
            postData.put("content", content);
            postData.put("timestamp", System.currentTimeMillis());
            
            String ipfsHash = ipfsService.uploadJson(postData);
            
            // Send event to Kafka for blockchain processing
            Map<String, String> event = new HashMap<>();
            event.put("type", "POST_CREATED");
            event.put("wallet", walletAddress);
            event.put("ipfsHash", ipfsHash);
            
            kafkaTemplate.send("blockchain-events", walletAddress, 
                new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(event));
            
            return BlockchainResponse.builder()
                .success(true)
                .ipfsHash(ipfsHash)
                .ipfsUrl(ipfsService.getGatewayUrl(ipfsHash))
                .message("Post content stored on IPFS. Blockchain transaction pending.")
                .build();
                
        } catch (Exception e) {
            return BlockchainResponse.builder()
                .success(false)
                .message("Failed to create post: " + e.getMessage())
                .build();
        }
    }

    /**
     * Store profile data on IPFS
     */
    public BlockchainResponse createProfile(String walletAddress, String username, String bio) {
        try {
            Map<String, Object> profileData = new HashMap<>();
            profileData.put("wallet", walletAddress);
            profileData.put("username", username);
            profileData.put("bio", bio);
            profileData.put("createdAt", System.currentTimeMillis());
            
            String ipfsHash = ipfsService.uploadJson(profileData);
            
            // Send event to Kafka
            Map<String, String> event = new HashMap<>();
            event.put("type", "PROFILE_CREATED");
            event.put("wallet", walletAddress);
            event.put("username", username);
            event.put("ipfsHash", ipfsHash);
            
            kafkaTemplate.send("blockchain-events", walletAddress,
                new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(event));
            
            return BlockchainResponse.builder()
                .success(true)
                .ipfsHash(ipfsHash)
                .ipfsUrl(ipfsService.getGatewayUrl(ipfsHash))
                .message("Profile stored on IPFS. Blockchain transaction pending.")
                .build();
                
        } catch (Exception e) {
            return BlockchainResponse.builder()
                .success(false)
                .message("Failed to create profile: " + e.getMessage())
                .build();
        }
    }

    /**
     * Process like and request token reward
     */
    public BlockchainResponse processLike(String walletAddress, String postAuthor, Long postId) {
        try {
            Map<String, String> event = new HashMap<>();
            event.put("type", "POST_LIKED");
            event.put("liker", walletAddress);
            event.put("postAuthor", postAuthor);
            event.put("postId", postId.toString());
            
            kafkaTemplate.send("blockchain-events", walletAddress,
                new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(event));
            
            return BlockchainResponse.builder()
                .success(true)
                .message("Like recorded. Token reward will be distributed.")
                .build();
                
        } catch (Exception e) {
            return BlockchainResponse.builder()
                .success(false)
                .message("Failed to process like: " + e.getMessage())
                .build();
        }
    }

    /**
     * Get user's token balance (from contract)
     */
    public TokenBalanceResponse getTokenBalance(String walletAddress) {
        // This would interact with the smart contract directly
        // For now, return a placeholder response
        return TokenBalanceResponse.builder()
            .wallet(walletAddress)
            .balance("0")
            .symbol("W3S")
            .decimals(18)
            .message("Token balance query - contract integration pending")
            .build();
    }

    /**
     * Get user's reward summary
     */
    public RewardSummaryResponse getRewardSummary(String walletAddress) {
        return RewardSummaryResponse.builder()
            .wallet(walletAddress)
            .totalEarned("0")
            .todayEarned("0")
            .dailyLimit("100")
            .postsCount(0)
            .likesCount(0)
            .commentsCount(0)
            .build();
    }

    /**
     * Check if user exists on blockchain
     */
    public CompletableFuture<Boolean> userExistsOnChain(String walletAddress) {
        // This would query the smart contract
        return CompletableFuture.completedFuture(false);
    }

    /**
     * Check if post exists on blockchain
     */
    public CompletableFuture<Boolean> postExistsOnChain(Long postId) {
        // This would query the smart contract
        return CompletableFuture.completedFuture(false);
    }
}
