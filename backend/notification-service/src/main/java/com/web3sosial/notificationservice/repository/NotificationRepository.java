package com.web3sosial.notificationservice.repository;

import com.web3sosial.notificationservice.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByRecipientAddressOrderByCreatedAtDesc(String recipientAddress);
    List<Notification> findByRecipientAddressAndIsReadFalse(String recipientAddress);
    Integer countByRecipientAddressAndIsReadFalse(String recipientAddress);
}
