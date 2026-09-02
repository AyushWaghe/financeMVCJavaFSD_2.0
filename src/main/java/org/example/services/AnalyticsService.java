package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dao.AnalyticsRepository;
import org.example.dao.MonthlyTransactionSummaryRepository;
import org.example.dto.*;
import org.example.models.MonthlyTransactionSummary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {

    private final AnalyticsRepository analyticsRepository;
    private final MonthlyTransactionSummaryRepository monthlyTransactionSummaryRepository;

    public List<MonthlySpendingResponse> getMonthlySavings(Integer userId,Integer year) {

        List<Object[]> result =
                monthlyTransactionSummaryRepository.getMonthlySavings(
                        userId,
                        year
                );

        return result.stream()
                .map(row -> new MonthlySpendingResponse(
                        (Integer) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
    }

    public MonthStatsResponse getMonthStats(Integer userId, Integer month, Integer year) {
        Optional<MonthlyTransactionSummary> monthlyTransactionSummary =
                monthlyTransactionSummaryRepository.findByUserUserIdAndYearAndMonth(
                        userId,
                        year,
                        month
                );

        if (monthlyTransactionSummary.isEmpty()) {
            return MonthStatsResponse.builder()
                    .totalIncome(BigDecimal.ZERO)
                    .totalExpense(BigDecimal.ZERO)
                    .totalSavings(BigDecimal.ZERO)
                    .totalNeedsExpense(BigDecimal.ZERO)
                    .totalWantsExpense(BigDecimal.ZERO)
                    .build();
        }

        MonthlyTransactionSummary summary = monthlyTransactionSummary.get();

        return MonthStatsResponse.builder()
                .totalIncome(summary.getTotalIncome())
                .totalExpense(summary.getTotalExpense())
                .totalSavings(summary.getTotalSavings())
                .totalNeedsExpense(summary.getTotalNeedExpense())
                .totalWantsExpense(summary.getTotalWantExpense())
                .build();
    }

    public List<MonthlyNeedsWantsResponse> getMonthlyNeedsWants(
            Integer userId,Integer year) {

        List<Object[]> result =
                monthlyTransactionSummaryRepository.getMonthlyNeedsWants(
                        userId,
                        year
                );

        return result.stream()
                .map(row -> new MonthlyNeedsWantsResponse(
                        (Integer) row[0],
                        (BigDecimal) row[1],
                        (BigDecimal) row[2]
                ))
                .toList();
    }

    public List<MonthlyCategoryResponse> getCategoryWise(Integer userId,Integer year,Integer month) {
        List<Object[]> result =
                analyticsRepository.getCategoryWiseSpending(
                        userId,
                        year,
                        month
                );

        return result.stream()
                .map(row -> new MonthlyCategoryResponse(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
    }

    public List<MonthlyIncomeExpenseReponse> getMonthlyIncomeVsExpense(
            Integer userId,Integer year) {

        List<Object[]> result =
                monthlyTransactionSummaryRepository.getMonthlyIncomeVsExpense(
                        userId,
                        year
                );

        return result.stream()
                .map(row -> new MonthlyIncomeExpenseReponse(
                        (Integer) row[0],
                        (BigDecimal) row[1],
                        (BigDecimal) row[2]
                ))
                .toList();
    }
}
