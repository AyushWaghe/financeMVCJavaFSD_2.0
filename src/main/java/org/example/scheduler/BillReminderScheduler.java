package org.example.scheduler;

import lombok.RequiredArgsConstructor;
import org.example.services.BillInstanceService;
import org.example.services.BillService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BillReminderScheduler {

    private final BillInstanceService billInstanceService;

    @Scheduled(cron = "0 0 0 * * *")
//    @Scheduled(cron = "0 * * * * *")
    public void processDueBills(){
        System.out.println("running shceduler");
        billInstanceService.processDueBills();
    }


}
