package org.example.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class EmailService {
    private final JavaMailSender mailSender;
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

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
        log.info("Bill reminder email accepted by SMTP for: " + recipientEmail);
    }

    public void sendAlertNotification(
            String recipientEmail,
            BigDecimal needsPercentage,
            BigDecimal wantsPercentage
    ) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo(recipientEmail);
        message.setSubject("Alert notification for Needs/Wants spendings: !!!");

        log.info("Sending bill reminder email from: " + fromEmail);
        log.info("Sending bill reminder email to: " + recipientEmail);

        message.setText("""
                Hello,

                You are about to cross your set limits for the current month. Below are the details-:
                
                Needs percentage-:%s
                Wants percentage-:%s

                -Savings saga
                """.formatted( needsPercentage,wantsPercentage));

        mailSender.send(message);
        log.info("Alert notification email accepted by SMTP for: " + recipientEmail);
    }
}
