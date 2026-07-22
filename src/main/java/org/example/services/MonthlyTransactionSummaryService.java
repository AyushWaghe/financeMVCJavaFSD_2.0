package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dao.MonthlyTransactionSummaryRepository;
import org.example.enums.SpendingType;
import org.example.enums.TransactionType;
import org.example.event.BillReminderEvent;
import org.example.event.MonthlySummaryUpdatedEvent;
import org.example.models.MonthlyTransactionSummary;
import org.example.models.Transaction;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MonthlyTransactionSummaryService {

    private final MonthlyTransactionSummaryRepository monthlyTransactionSummaryRepository;
    private final KafkaTemplate<String, MonthlySummaryUpdatedEvent> kafkaTemplate;


    @Transactional
    public void updateMonthlySummaryOnTransactionCreate(Transaction transaction) {

        int year = transaction.getTransactionDate().getYear();
        int month = transaction.getTransactionDate().getMonthValue();
        Integer userId=transaction.getUser().getUserId();
        BigDecimal amount = transaction.getAmount();

        MonthlyTransactionSummary summary =
                monthlyTransactionSummaryRepository
                        .findByUserUserIdAndYearAndMonth(transaction.getUser().getUserId(), year, month)
                        .orElseGet(() -> {
                            MonthlyTransactionSummary mts = new MonthlyTransactionSummary();
                            mts.setUser(transaction.getUser());
                            mts.setYear(year);
                            mts.setMonth(month);
                            return monthlyTransactionSummaryRepository.save(mts);
                        });


        updateMonthlySummaryDelta(transaction,month,year,amount,userId);

        if(year==LocalDate.now().getYear() && month==LocalDate.now().getMonthValue()){
            MonthlySummaryUpdatedEvent monthlySummaryUpdatedEvent=new MonthlySummaryUpdatedEvent(userId);
            kafkaTemplate.send(
                    "monthly-summary-updated-topic",
                    userId.toString(),
                    monthlySummaryUpdatedEvent
            );
        }

    }

    @Transactional
    public void updateMonthlySummaryOnTransactionUpdate(Transaction oldTransaction,Transaction newTransaction) {

        int oldYear = oldTransaction.getTransactionDate().getYear();
        int oldMonth = oldTransaction.getTransactionDate().getMonthValue();
        int newYear = newTransaction.getTransactionDate().getYear();
        int newMonth = newTransaction.getTransactionDate().getMonthValue();
        Integer userId = oldTransaction.getUser().getUserId();
        BigDecimal oldAmount = oldTransaction.getAmount().negate();
        BigDecimal newAmount = newTransaction.getAmount();
        System.out.println("Old year is"+oldYear);

        if(newYear!=oldYear || newMonth!=oldMonth){ //Transaction date updated hence monthly entries to be updated

            monthlyTransactionSummaryRepository //First check if the new entry for monthly summary table exists
                    .findByUserUserIdAndYearAndMonth(
                            newTransaction.getUser().getUserId(),
                            newYear,
                            newMonth
                    )
                    .orElseGet(() -> {
                        MonthlyTransactionSummary summary = new MonthlyTransactionSummary();
                        summary.setUser(newTransaction.getUser());
                        summary.setYear(newYear);
                        summary.setMonth(newMonth);
                        return monthlyTransactionSummaryRepository.save(summary);
                    });

            //Remove the old transaction from the monthly summary table

            updateMonthlySummaryDelta(oldTransaction,oldMonth,oldYear,oldAmount,userId);

            //Add the new transaction entry in the new month

            updateMonthlySummaryDelta(newTransaction,newMonth,newYear,newAmount,userId);

            //Kafka event publish
            if((oldYear==LocalDate.now().getYear() && oldMonth==LocalDate.now().getMonthValue())
                    || (newYear==LocalDate.now().getYear() && newMonth==LocalDate.now().getMonthValue())){
                MonthlySummaryUpdatedEvent monthlySummaryUpdatedEvent=new MonthlySummaryUpdatedEvent(userId);
                kafkaTemplate.send(
                        "monthly-summary-updated-topic",
                        userId.toString(),
                        monthlySummaryUpdatedEvent
                );
            }

        }else{
            if(oldTransaction.getType() != newTransaction.getType() || oldTransaction.getSpendingType() != newTransaction.getSpendingType()
                    || oldTransaction.getAmount()
                    .compareTo(newTransaction.getAmount()) != 0){
                updateMonthlySummaryDelta(oldTransaction,oldMonth,oldYear,oldAmount,userId);
                updateMonthlySummaryDelta(newTransaction,newMonth,newYear,newAmount,userId);

                //Kafka even publish
                if((oldYear==LocalDate.now().getYear() && oldMonth==LocalDate.now().getMonthValue())
                        || (newYear==LocalDate.now().getYear() && newMonth==LocalDate.now().getMonthValue())){
                    MonthlySummaryUpdatedEvent monthlySummaryUpdatedEvent=new MonthlySummaryUpdatedEvent(userId);
                    kafkaTemplate.send(
                            "monthly-summary-updated-topic",
                            userId.toString(),
                            monthlySummaryUpdatedEvent
                    );
                }
            }
        }
    }

    @Transactional
    public void updateMonthlySummaryOnTransactionDelete(Transaction transaction) {
        int year = transaction.getTransactionDate().getYear();
        int month = transaction.getTransactionDate().getMonthValue();
        Integer userId=transaction.getUser().getUserId();
        BigDecimal amount = transaction.getAmount().negate();

        updateMonthlySummaryDelta(transaction,month,year,amount,userId);

        //Kafka event publish
        if(year==LocalDate.now().getYear() && month==LocalDate.now().getMonthValue()){
            MonthlySummaryUpdatedEvent monthlySummaryUpdatedEvent=new MonthlySummaryUpdatedEvent(userId);
            kafkaTemplate.send(
                    "monthly-summary-updated-topic",
                    userId.toString(),
                    monthlySummaryUpdatedEvent
            );
        }

    }

    //Helper method ==================================================================
    private void updateMonthlySummaryDelta(Transaction transaction,Integer month,Integer year,BigDecimal changeAmount,Integer userId){
        if (transaction.getType() == TransactionType.INCOME) {
            monthlyTransactionSummaryRepository.updateIncome(
                    userId, year, month, changeAmount
            );
        } else {
            switch (transaction.getSpendingType()) {
                case NEEDS -> monthlyTransactionSummaryRepository.updateNeedsExpense(
                        userId, year, month, changeAmount
                );

                case WANTS -> monthlyTransactionSummaryRepository.updateWantsExpense(
                        userId, year, month, changeAmount
                );

                case SAVINGS ->{
                    System.out.println("When in savings");
                    monthlyTransactionSummaryRepository.updateSavingsExpense(
                            userId, year, month, changeAmount
                    );
                    System.out.println("Reached here");
                }
            }
        }
    }


}
