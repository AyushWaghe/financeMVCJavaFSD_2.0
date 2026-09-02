package org.example.event;

import java.math.BigDecimal;

public record AlertNotificationEvent(
        Integer userId,
        Integer month,
        Integer year,
        BigDecimal needsPercentage,
        BigDecimal wantsPercentage,
        String userEmail
) {}
