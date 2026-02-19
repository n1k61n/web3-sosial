package com.web3sosial.blockchain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenBalanceResponse {
    private String wallet;
    private String balance;
    private String symbol;
    private int decimals;
    private String message;
}
