package org.example.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendBillReminder(
            String recipientEmail,
            String billTitle,
            BigDecimal amount,
            LocalDate dueDate
    ) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo(recipientEmail);
        message.setSubject("Bill reminder: !!!" + billTitle);

        System.out.println("Sending bill reminder email from: " + fromEmail);
        System.out.println("Sending bill reminder email to: " + recipientEmail);

        message.setText("""
                Hello,

                This is a reminder for your upcoming bill.Below are the details

                Bill: %s
                Amount: ₹%s
                Due date: %s

                Please pay it before the due date.

                -Savings saga
                """.formatted(billTitle, amount, dueDate));

        mailSender.send(message);
        System.out.println("Bill reminder email accepted by SMTP for: " + recipientEmail);
    }
}
