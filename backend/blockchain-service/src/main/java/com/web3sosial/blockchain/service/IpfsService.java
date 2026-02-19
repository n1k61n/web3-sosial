package com.web3sosial.blockchain.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class IpfsService {
    
    @Value("${ipfs.api-address:/ip4/127.0.0.1/tcp/5001}")
    private String apiAddress;
    
    @Value("${ipfs.gateway-url:http://localhost:8080/ipfs/}")
    private String gatewayUrl;
    
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public IpfsService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Upload text content to IPFS via HTTP API
     */
    public String uploadContent(String content) throws IOException, InterruptedException {
        // For now, return a mock hash - in production, connect to actual IPFS node
        // This is a placeholder implementation
        String mockHash = "Qm" + Base64.getUrlEncoder().withoutPadding()
            .encodeToString(content.substring(0, Math.min(10, content.length())).getBytes());
        return mockHash;
    }

    /**
     * Upload JSON data to IPFS
     */
    public String uploadJson(Map<String, Object> jsonData) throws IOException, InterruptedException {
        String jsonString = objectMapper.writeValueAsString(jsonData);
        return uploadContent(jsonString);
    }

    /**
     * Get content from IPFS by hash
     */
    public String getContent(String hash) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(gatewayUrl + hash))
            .GET()
            .build();
            
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    /**
     * Get IPFS gateway URL for a hash
     */
    public String getGatewayUrl(String hash) {
        return gatewayUrl + hash;
    }

    /**
     * Pin content to keep it permanently
     */
    public void pinContent(String hash) throws IOException, InterruptedException {
        // Placeholder - implement with actual IPFS pin API
        System.out.println("Pinning content: " + hash);
    }

    /**
     * Check if content is pinned
     */
    public boolean isPinned(String hash) throws IOException, InterruptedException {
        // Placeholder - implement with actual IPFS pin API
        return true;
    }
}
