package org.example.consumer;

import org.example.event.MonthlySummaryUpdatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MonthlySummaryUpdatedConsumer {
    @KafkaListener(
            topics = "monthly-summary-updated-topic",
            groupId = "monthly-summary-updated-group"
    )
    public void monthlySummaryUpdated(MonthlySummaryUpdatedEvent monthlySummaryUpdatedEvent){
        System.out.println("Recieved monthly summary updated event");
        System.out.println(monthlySummaryUpdatedEvent);
    }
}
