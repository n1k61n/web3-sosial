package com.web3social.authservice.security;

import org.springframework.stereotype.Service;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;

@Service
public class Web3SignatureService {

    public boolean verifySignature(String walletAddress, String message, String signature) {
        try {
            String prefix = "\u0019Ethereum Signed Message:\n" + message.length();
            byte[] msgHash = Hash.sha3((prefix + message).getBytes());

            byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
            byte v = signatureBytes[64];
            if (v < 27) v += 27;

            Sign.SignatureData signatureData = new Sign.SignatureData(
                    v,
                    Arrays.copyOfRange(signatureBytes, 0, 32),
                    Arrays.copyOfRange(signatureBytes, 32, 64)
            );

            BigInteger publicKey = Sign.signedMessageHashToKey(msgHash, signatureData);
            String recoveredAddress = "0x" + Keys.getAddress(publicKey);

            return recoveredAddress.equalsIgnoreCase(walletAddress);
        } catch (Exception e) {
            return false;
        }
    }
}