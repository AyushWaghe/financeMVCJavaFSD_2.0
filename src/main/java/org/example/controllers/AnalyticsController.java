package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.services.AnalyticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/monthly-spendings")
    public ResponseEntity<APIResponse<List<MonthlySpendingResponse>>> getMonthWise(@Valid @RequestBody MonthlySpendingRequest monthlySpendingRequest){
        List<MonthlySpendingResponse> response =
                analyticsService.getMonthlySpendings(monthlySpendingRequest);

        APIResponse<List<MonthlySpendingResponse>> apiResponse=new APIResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setData(response);
        apiResponse.setMessage("Monthly spendings fetched successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/monthly-needs-wants")
    public ResponseEntity<APIResponse<List<MonthlyNeedsWantsResponse>>> getMonthNeedsWants(@Valid @RequestBody MonthlySpendingRequest monthlySpendingRequest){
        List<MonthlyNeedsWantsResponse> response =
                analyticsService.getMonthlyNeedsWants(monthlySpendingRequest);

        APIResponse<List<MonthlyNeedsWantsResponse>> apiResponse=new APIResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setData(response);
        apiResponse.setMessage("Monthly needs/wants fetched successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/monthly-category")
    public ResponseEntity<APIResponse<List<MonthlyCategoryResponse>>> getCategoryWise(@Valid @RequestBody MonthlyCategoryRequest monthlyCategoryRequest){
        List<MonthlyCategoryResponse> response =
                analyticsService.getCategoryWise(monthlyCategoryRequest);

        APIResponse<List<MonthlyCategoryResponse>> apiResponse=new APIResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setData(response);
        apiResponse.setMessage("Monthly category wise fetched successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/income-vs-expense")
    public ResponseEntity<APIResponse<List<MonthlyIncomeExpenseReponse>>> getMonthlyIncomeExpense(@Valid @RequestBody MonthlySpendingRequest monthlySpendingRequest){
        List<MonthlyIncomeExpenseReponse> response =
                analyticsService.getMonthlyIncomeVsExpense(monthlySpendingRequest);

        APIResponse<List<MonthlyIncomeExpenseReponse>> apiResponse=new APIResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setData(response);
        apiResponse.setMessage("Monthly income vs expense fetched successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }




}
