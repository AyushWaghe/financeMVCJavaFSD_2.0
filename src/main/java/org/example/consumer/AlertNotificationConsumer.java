package org.example.consumer;

import org.example.event.AlertNotificationEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AlertNotificationConsumer {
    @KafkaListener(
            topics = "alert-notification-topic",
            groupId = "alert-notification-group"
    )
    public void alertNotificationConsumer(AlertNotificationEvent alertNotificationEvent){
        System.out.println("Alert notification service ");
        System.out.println(alertNotificationEvent);
    }
}
