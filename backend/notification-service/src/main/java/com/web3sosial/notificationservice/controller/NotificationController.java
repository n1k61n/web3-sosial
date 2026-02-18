package com.web3sosial.notificationservice.controller;

import com.web3sosial.notificationservice.dto.NotificationDto;
import com.web3sosial.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{walletAddress}")
    public ResponseEntity<List<NotificationDto>> getNotifications(@PathVariable String walletAddress) {
        return ResponseEntity.ok(notificationService.getNotifications(walletAddress));
    }

    @GetMapping("/{walletAddress}/unread")
    public ResponseEntity<List<NotificationDto>> getUnreadNotifications(@PathVariable String walletAddress) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(walletAddress));
    }

    @GetMapping("/{walletAddress}/count")
    public ResponseEntity<Integer> getUnreadCount(@PathVariable String walletAddress) {
        return ResponseEntity.ok(notificationService.getUnreadCount(walletAddress));
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable String notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{walletAddress}/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable String walletAddress) {
        notificationService.markAllAsRead(walletAddress);
        return ResponseEntity.ok().build();
    }
}
