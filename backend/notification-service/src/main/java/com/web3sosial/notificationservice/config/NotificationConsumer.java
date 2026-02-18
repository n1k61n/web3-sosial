package com.web3sosial.notificationservice.config;

import com.web3sosial.notificationservice.dto.NotificationEvent;
import com.web3sosial.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "notifications", groupId = "notification-group")
    public void consume(NotificationEvent event) {
        log.info("Received notification event: {}", event);
        notificationService.createNotification(event);
    }
}
