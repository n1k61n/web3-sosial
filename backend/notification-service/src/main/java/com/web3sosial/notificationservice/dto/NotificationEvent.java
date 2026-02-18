package com.web3sosial.notificationservice.dto;

import com.web3sosial.notificationservice.entity.NotificationType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent {
    private String recipientAddress;
    private String senderAddress;
    private NotificationType type;
    private String referenceId;
    private String message;
}
