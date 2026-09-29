package org.example.consumer;

import lombok.RequiredArgsConstructor;
import org.example.event.AlertNotificationEvent;
import org.example.services.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class AlertNotificationConsumer {

    private final EmailService emailService;

    @KafkaListener(
            topics = "alert-notification-topic",
            groupId = "alert-notification-group"
    )
    public void alertNotificationConsumer(AlertNotificationEvent alertNotificationEvent){
        emailService.sendAlertNotification(
                alertNotificationEvent.userEmail(),
                alertNotificationEvent.needsPercentage(),
                alertNotificationEvent.wantsPercentage()
        );
    }
}
