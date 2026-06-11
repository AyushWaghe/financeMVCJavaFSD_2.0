package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dao.AnalyticsRepository;
import org.example.dto.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsRepository analyticsRepository;

    public List<MonthlySpendingResponse> getMonthlySpendings(MonthlySpendingRequest monthlySpendingRequest) {

        List<Object[]> result =
                analyticsRepository.getMonthlyExpenses(
                        monthlySpendingRequest.getUserId(),
                        monthlySpendingRequest.getYear()
                );

        return result.stream()
                .map(row -> new MonthlySpendingResponse(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
    }

    public List<MonthlyNeedsWantsResponse> getMonthlyNeedsWants(MonthlySpendingRequest monthlySpendingRequest) {
        List<Object[]> result =
                analyticsRepository.getMonthlyNeedsWants(
                        monthlySpendingRequest.getUserId(),
                        monthlySpendingRequest.getYear()
                );

        return result.stream()
                .map(row -> new MonthlyNeedsWantsResponse(
                        (String) row[0],
                        (BigDecimal) row[1],
                        (BigDecimal) row[2]
                ))
                .toList();
    }

    public List<MonthlyCategoryResponse> getCategoryWise(MonthlyCategoryRequest monthlyCategoryRequest) {
        List<Object[]> result =
                analyticsRepository.getCategoryWiseSpending(
                        monthlyCategoryRequest.getUserId(),
                        monthlyCategoryRequest.getYear(),
                        monthlyCategoryRequest.getMonth()
                );

        return result.stream()
                .map(row -> new MonthlyCategoryResponse(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
    }

    public List<MonthlyIncomeExpenseReponse> getMonthlyIncomeVsExpense(MonthlySpendingRequest monthlySpendingRequest) {
        List<Object[]> result =
                analyticsRepository.getMonthlyIncomeVsExpense(
                        monthlySpendingRequest.getUserId(),
                        monthlySpendingRequest.getYear()
                );

        return result.stream()
                .map(row -> new MonthlyIncomeExpenseReponse(
                        (String) row[0],
                        (BigDecimal) row[1],
                        (BigDecimal) row[2]
                ))
                .toList();
    }
}
