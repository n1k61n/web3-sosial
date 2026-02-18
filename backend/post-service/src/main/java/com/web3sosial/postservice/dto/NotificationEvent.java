package com.web3sosial.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent {
    private String recipientAddress;
    private String senderAddress;
    private String type;
    private String referenceId;
    private String message;
}