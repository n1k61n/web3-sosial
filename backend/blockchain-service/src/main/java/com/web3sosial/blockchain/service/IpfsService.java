package com.web3sosial.blockchain.service;

import io.ipfs.api.IPFS;
import io.ipfs.api.TypedFile;
import io.ipfs.multihash.Multihash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@Service
public class IpfsService {

    private final IPFS ipfs;
    
    @Value("${ipfs.gateway-url:http://localhost:8080/ipfs/}")
    private String gatewayUrl;

    public IpfsService(@Value("${ipfs.api-address:/ip4/127.0.0.1/tcp/5001}") String apiAddress) {
        this.ipfs = new IPFS(apiAddress);
    }

    /**
     * Upload text content to IPFS
     */
    public String uploadContent(String content) throws IOException {
        // Create temporary file
        File tempFile = File.createTempFile("ipfs-content", ".txt");
        tempFile.deleteOnExit();
        
        Files.writeString(tempFile.toPath(), content);
        
        // Upload to IPFS
        List<Multihash> hashes = ipfs.add(List.of(new TypedFile(tempFile)));
        String hash = hashes.get(0).toString();
        
        return hash;
    }

    /**
     * Upload JSON data to IPFS
     */
    public String uploadJson(Map<String, Object> jsonData) throws IOException {
        String jsonString = new com.fasterxml.jackson.databind.ObjectMapper()
                .writeValueAsString(jsonData);
        return uploadContent(jsonString);
    }

    /**
     * Get content from IPFS by hash
     */
    public String getContent(String hash) throws IOException {
        Multihash multihash = Multihash.fromBase58(hash);
        byte[] content = ipfs.cat(multihash);
        return new String(content);
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
    public void pinContent(String hash) throws IOException {
        Multihash multihash = Multihash.fromBase58(hash);
        ipfs.pin.add(multihash);
    }

    /**
     * Check if content is pinned
     */
    public boolean isPinned(String hash) throws IOException {
        Multihash multihash = Multihash.fromBase58(hash);
        Map<String, Object> pins = ipfs.pin.ls();
        return pins.containsKey(hash);
    }
}
