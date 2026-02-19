package com.web3sosial.blockchain.service;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class Web3jService {

    private final Web3j web3j;
    private final StaticGasProvider gasProvider;

    public Web3jService(
            @Value("${web3j.client-address:http://localhost:8545}") String clientAddress,
            @Value("${blockchain.gas-price:20000000000}") long gasPrice,
            @Value("${blockchain.gas-limit:4000000}") long gasLimit) {
        
        this.web3j = Web3j.build(new HttpService(clientAddress));
        this.gasProvider = new StaticGasProvider(
            BigInteger.valueOf(gasPrice),
            BigInteger.valueOf(gasLimit)
        );
    }

    /**
     * Get current block number
     */
    public Long getCurrentBlockNumber() throws Exception {
        EthBlockNumber ethBlockNumber = web3j.ethBlockNumber()
                .sendAsync()
                .get();
        return ethBlockNumber.getBlockNumber().longValue();
    }

    /**
     * Get ETH balance of an address
     */
    public Double getEthBalance(String address) throws Exception {
        EthGetBalance ethGetBalance = web3j.ethGetBalance(
                address,
                DefaultBlockParameterName.LATEST
        ).sendAsync().get();
        
        return ethGetBalance.getBalance().doubleValue() / 1e18;
    }

    /**
     * Check if address is valid
     */
    public boolean isValidAddress(String address) {
        return address != null && address.matches("^0x[a-fA-F0-9]{40}$");
    }

    /**
     * Get Web3j instance for direct access
     */
    public Web3j getWeb3j() {
        return web3j;
    }

    /**
     * Get gas provider
     */
    public StaticGasProvider getGasProvider() {
        return gasProvider;
    }
}
