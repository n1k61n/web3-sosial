package com.web3sosial.notificationservice.service;

import com.web3sosial.notificationservice.dto.NotificationDto;
import com.web3sosial.notificationservice.dto.NotificationEvent;
import com.web3sosial.notificationservice.entity.Notification;
import com.web3sosial.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void createNotification(NotificationEvent event) {
        Notification notification = Notification.builder()
                .recipientAddress(event.getRecipientAddress())
                .senderAddress(event.getSenderAddress())
                .type(event.getType())
                .referenceId(event.getReferenceId())
                .message(event.getMessage())
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }

    public List<NotificationDto> getNotifications(String walletAddress) {
        return notificationRepository
                .findByRecipientAddressOrderByCreatedAtDesc(walletAddress)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<NotificationDto> getUnreadNotifications(String walletAddress) {
        return notificationRepository
                .findByRecipientAddressAndIsReadFalse(walletAddress)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Integer getUnreadCount(String walletAddress) {
        return notificationRepository.countByRecipientAddressAndIsReadFalse(walletAddress);
    }

    public void markAsRead(String notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setIsRead(true);
            notificationRepository.save(n);
        });
    }

    public void markAllAsRead(String walletAddress) {
        List<Notification> unread = notificationRepository
                .findByRecipientAddressAndIsReadFalse(walletAddress);
        unread.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(unread);
    }

    private NotificationDto mapToDto(Notification n) {
        return NotificationDto.builder()
                .id(n.getId())
                .recipientAddress(n.getRecipientAddress())
                .senderAddress(n.getSenderAddress())
                .type(n.getType())
                .referenceId(n.getReferenceId())
                .message(n.getMessage())
                .isRead(n.getIsRead())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
