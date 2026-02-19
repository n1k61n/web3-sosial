package com.web3sosial.blockchain.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Kafka listener for blockchain events
 * Listens to blockchain-events topic and processes on-chain transactions
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BlockchainEventListener {

    private final ObjectMapper objectMapper;

    /**
     * Listen to blockchain events from Kafka
     * Events include: POST_CREATED, PROFILE_CREATED, POST_LIKED, COMMENT_ADDED, FOLLOWED
     */
    @KafkaListener(topics = "blockchain-events", groupId = "blockchain-service")
    public void listen(String message) {
        try {
            log.info("Received blockchain event: {}", message);
            
            Map<String, Object> event = objectMapper.readValue(message, Map.class);
            String eventType = (String) event.get("type");
            
            switch (eventType) {
                case "POST_CREATED" -> handlePostCreated(event);
                case "PROFILE_CREATED" -> handleProfileCreated(event);
                case "POST_LIKED" -> handlePostLiked(event);
                case "COMMENT_ADDED" -> handleCommentAdded(event);
                case "FOLLOWED" -> handleFollowed(event);
                default -> log.warn("Unknown event type: {}", eventType);
            }
            
        } catch (Exception e) {
            log.error("Error processing blockchain event: {}", message, e);
        }
    }

    private void handlePostCreated(Map<String, Object> event) {
        String wallet = (String) event.get("wallet");
        String ipfsHash = (String) event.get("ipfsHash");
        log.info("Processing post creation for wallet: {} with IPFS hash: {}", wallet, ipfsHash);
        // TODO: Call smart contract to create post on-chain
    }

    private void handleProfileCreated(Map<String, Object> event) {
        String wallet = (String) event.get("wallet");
        String username = (String) event.get("username");
        log.info("Processing profile creation for user: {} ({})", username, wallet);
        // TODO: Call smart contract to create profile on-chain
    }

    private void handlePostLiked(Map<String, Object> event) {
        String liker = (String) event.get("liker");
        String postAuthor = (String) event.get("postAuthor");
        String postId = (String) event.get("postId");
        log.info("Processing like: {} liked post {} by {}", liker, postId, postAuthor);
        // TODO: Call smart contract to record like and distribute rewards
    }

    private void handleCommentAdded(Map<String, Object> event) {
        String commenter = (String) event.get("commenter");
        String postId = (String) event.get("postId");
        log.info("Processing comment on post {} by {}", postId, commenter);
        // TODO: Call smart contract to record comment
    }

    private void handleFollowed(Map<String, Object> event) {
        String follower = (String) event.get("follower");
        String following = (String) event.get("following");
        log.info("Processing follow: {} followed {}", follower, following);
        // TODO: Call smart contract to record follow
    }
}
