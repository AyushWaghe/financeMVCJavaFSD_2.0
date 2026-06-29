package org.example.consumer;

import lombok.RequiredArgsConstructor;
import org.example.dao.MonthlyTransactionSummaryRepository;
import org.example.dao.UserDetailsRepository;
import org.example.event.AlertNotificationEvent;
import org.example.event.MonthlySummaryUpdatedEvent;
import org.example.exceptions.ResourceNotFoundException;
import org.example.exceptions.UserDetailNotFoundException;
import org.example.models.MonthlyTransactionSummary;
import org.example.models.User;
import org.example.models.UserDetail;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MonthlySummaryUpdatedConsumer {

    private final MonthlyTransactionSummaryRepository monthlyTransactionSummaryRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final KafkaTemplate<String, AlertNotificationEvent> kafkaTemplate;

    @KafkaListener(
            topics = "monthly-summary-updated-topic",
            groupId = "monthly-summary-updated-group"
    )
    public void monthlySummaryUpdated(MonthlySummaryUpdatedEvent monthlySummaryUpdatedEvent){
        System.out.println("Recieved monthly summary updated event");

        Integer userId=monthlySummaryUpdatedEvent.userId();
        UserDetail userDetail=userDetailsRepository.findById(userId).orElseThrow(() -> new UserDetailNotFoundException("No user details found for user id"+userId));

        if(!userDetail.isNotificationSubscribed()) return;  //User has not subscribed to receive notification

        Integer year= LocalDate.now().getYear();
        Integer month=LocalDate.now().getMonthValue();

        MonthlyTransactionSummary monthlyTransactionSummary=monthlyTransactionSummaryRepository.findByUserUserIdAndYearAndMonth(userId,year,month)
                .orElseThrow(() -> new ResourceNotFoundException("No monthly summary found for user id"+userId));



        Integer lastNeedThresholdSend=monthlyTransactionSummary.getLastNeedThresholdSent();
        Integer lastWantThresholdSend=monthlyTransactionSummary.getLastWantThresholdSent();
        BigDecimal totalNeedsExpense=monthlyTransactionSummary.getTotalNeedExpense();
        BigDecimal totalWantsExpense=monthlyTransactionSummary.getTotalWantExpense();
        Integer needsLimit=userDetail.getNeeds();
        Integer wantsLimit=userDetail.getWants();
        Integer savings=userDetail.getSavings();

        BigDecimal needPercentage = BigDecimal.ZERO;
        BigDecimal wantPercentage = BigDecimal.ZERO;

        if (needsLimit != null && needsLimit > 0) {
            needPercentage = totalNeedsExpense
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(needsLimit), 2, RoundingMode.HALF_UP);
        }

        if (wantsLimit != null && wantsLimit > 0) {
            wantPercentage = totalWantsExpense
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(wantsLimit), 2, RoundingMode.HALF_UP);
        }

        //Needs event more than 100--------------------------------------------------
        if (needPercentage.compareTo(BigDecimal.valueOf(100)) >= 0 && needPercentage.compareTo(BigDecimal.ZERO) > 0) {

            if (lastNeedThresholdSend < 100) {

                monthlyTransactionSummary.setLastNeedThresholdSent(100);
                monthlyTransactionSummaryRepository.save(monthlyTransactionSummary);
                // Publish NEED 100% notification event
                AlertNotificationEvent alertNotificationEvent=new AlertNotificationEvent(userId,month,year,needPercentage,wantPercentage);
                kafkaTemplate.send(
                        "alert-notification-topic",
                        userId.toString(),
                        alertNotificationEvent
                );
            }
        } else if (needPercentage.compareTo(BigDecimal.valueOf(90)) >= 0 && needPercentage.compareTo(BigDecimal.ZERO) > 0) { //more than 90

            if (lastNeedThresholdSend < 90) {
                monthlyTransactionSummary.setLastNeedThresholdSent(90);
                monthlyTransactionSummaryRepository.save(monthlyTransactionSummary);
                // Publish NEED 90% notification event
                AlertNotificationEvent alertNotificationEvent=new AlertNotificationEvent(userId,month,year,needPercentage,wantPercentage);
                kafkaTemplate.send(
                        "alert-notification-topic",
                        userId.toString(),
                        alertNotificationEvent
                );
            }
        }else {
            if (lastNeedThresholdSend != 0 && needPercentage.compareTo(BigDecimal.ZERO) > 0) {  // <90 %
                monthlyTransactionSummary.setLastNeedThresholdSent(0);
                monthlyTransactionSummaryRepository.save(monthlyTransactionSummary);
            }
        }

        //Wants event------------------------------------------------------------------------------------------
        // >= 100%
        if (wantPercentage.compareTo(BigDecimal.valueOf(100)) >= 0 && wantPercentage.compareTo(BigDecimal.ZERO) > 0) {
            if (lastWantThresholdSend < 100) {
                monthlyTransactionSummary.setLastWantThresholdSent(100);
                monthlyTransactionSummaryRepository.save(monthlyTransactionSummary);
                // Publish WANT 100% notification event
                AlertNotificationEvent alertNotificationEvent=new AlertNotificationEvent(userId,month,year,needPercentage,wantPercentage);
                kafkaTemplate.send(
                        "alert-notification-topic",
                        userId.toString(),
                        alertNotificationEvent
                );
            }
        } else if (wantPercentage.compareTo(BigDecimal.valueOf(90)) >= 0 && wantPercentage.compareTo(BigDecimal.ZERO) > 0) { //Wants percentage more than 90
            if (lastWantThresholdSend < 90) {
                monthlyTransactionSummary.setLastWantThresholdSent(90);
                monthlyTransactionSummaryRepository.save(monthlyTransactionSummary);
                // Publish WANT 90% notification event
                AlertNotificationEvent alertNotificationEvent=new AlertNotificationEvent(userId,month,year,needPercentage,wantPercentage);
                kafkaTemplate.send(
                        "alert-notification-topic",
                        userId.toString(),
                        alertNotificationEvent
                );
            }
        }
        else {
            if (lastWantThresholdSend != 0 && wantPercentage.compareTo(BigDecimal.ZERO) > 0) { //Wants percentage less than 90
                monthlyTransactionSummary.setLastWantThresholdSent(0);
                monthlyTransactionSummaryRepository.save(monthlyTransactionSummary);
            }
        }

        System.out.println("kakfa processed");
    }
}
