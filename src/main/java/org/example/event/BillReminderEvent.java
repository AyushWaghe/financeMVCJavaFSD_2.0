package org.example.event;


import org.example.enums.BillStatus;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;

//Here we have used a record cause of immutability. As events represent immutability and java also generates all the getters and all for it
//Furthermore fields are generated as private final .... and so on by java
public record BillReminderEvent(
        Integer id,
        String title,
        BigDecimal amount,
        LocalDate dueDate,
        BillStatus status
) {}
