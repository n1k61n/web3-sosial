package com.web3sosial.notificationservice.dto;

import com.web3sosial.notificationservice.entity.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
    private String id;
    private String recipientAddress;
    private String senderAddress;
    private NotificationType type;
    private String referenceId;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
