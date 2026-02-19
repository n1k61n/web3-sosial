package com.web3sosial.blockchain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlockchainResponse {
    private boolean success;
    private String ipfsHash;
    private String ipfsUrl;
    private String transactionHash;
    private String message;
}
