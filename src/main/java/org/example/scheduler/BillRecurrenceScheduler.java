package org.example.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dao.BillInstanceRepository;
import org.example.dao.BillRepository;
import org.example.enums.BillRecurrence;
import org.example.enums.BillStatus;
import org.example.models.Bill;
import org.example.models.BillInstance;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BillRecurrenceScheduler {
    private final BillRepository billRepository;
    private final BillInstanceRepository billInstanceRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void generateRecurringBills() {

        LocalDate today = LocalDate.now();

        List<Bill> bills = billRepository.findBillsToGenerate(today);
        LocalDate nextDueDate;
        for (Bill bill : bills) {

            nextDueDate= calculateNextDueDate(
                    bill.getLatestDueDate(),
                    bill.getBillRecurrence()
            );

            BillInstance billInstance = new BillInstance();
            billInstance.setUser(bill.getUser());
            billInstance.setTitle(bill.getTitle());
            billInstance.setAmount(bill.getAmount());
            billInstance.setDueDate(nextDueDate);
            billInstance.setBillStatus(BillStatus.PENDING);

            billInstanceRepository.save(billInstance);

            bill.setLatestDueDate(nextDueDate);
        }
    }

    private LocalDate calculateNextDueDate(LocalDate currentDueDate,
                                           BillRecurrence recurrence) {

        return switch (recurrence) {
            case DAILY -> currentDueDate.plusDays(1);
            case WEEKLY -> currentDueDate.plusWeeks(1);
            case MONTHLY -> currentDueDate.plusMonths(1);
            case QUARTERLY -> currentDueDate.plusMonths(3);
            case YEARLY -> currentDueDate.plusYears(1);
            case NONE -> currentDueDate;
            default -> throw new IllegalStateException("Unexpected value: " + recurrence);
        };
    }
}
