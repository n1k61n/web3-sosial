package com.web3sosial.notificationservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String recipientAddress;

    @Column(nullable = false)
    private String senderAddress;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column
    private String referenceId;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column
    private Boolean isRead = false;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
