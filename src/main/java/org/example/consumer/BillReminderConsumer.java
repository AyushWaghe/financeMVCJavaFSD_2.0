package org.example.consumer;

import org.example.event.BillReminderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BillReminderConsumer {

    @KafkaListener(
            topics = "bill-reminder-topic",
            groupId = "bill-reminder-group"
    )
    public void billReminderNotificationService(BillReminderEvent billReminderEvent){
        System.out.println("Recieved bill reminder");
        System.out.println(billReminderEvent);
    }
}
