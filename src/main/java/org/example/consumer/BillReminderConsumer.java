package org.example.consumer;

import org.example.dao.BillInstanceRepository;
import org.example.event.BillReminderEvent;
import org.example.models.BillInstance;
import org.example.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BillReminderConsumer {

    @Autowired
    BillInstanceRepository billInstanceRepository;

    @Autowired
    EmailService emailService;

    @KafkaListener(
            topics = "bill-reminder-topic",
            groupId = "bill-reminder-group"
    )
    public void billReminderNotificationService(BillReminderEvent billReminderEvent){
        BillInstance bill = billInstanceRepository.findById(billReminderEvent.id())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Bill instance not found: " + billReminderEvent.id()
                ));

        String userEmail = bill.getUser().getUseremail();

        System.out.println("Recieved bill reminder");
        System.out.println(billReminderEvent);

        emailService.sendBillReminder(
                userEmail,
                billReminderEvent.title(),
                billReminderEvent.amount(),
                billReminderEvent.dueDate()
        );
    }
}
