package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.services.AnalyticsService;
import org.example.utils.AuthenticationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/monthly-savings/user")
    public ResponseEntity<APIResponse<List<MonthlySpendingResponse>>> getMonthlySavings(@RequestParam("year") Integer year){
        Integer userId= AuthenticationUtil.getCurrentUserId();
        List<MonthlySpendingResponse> response =
                analyticsService.getMonthlySavings(userId,year);

        APIResponse<List<MonthlySpendingResponse>> apiResponse=new APIResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setData(response);
        apiResponse.setMessage("Monthly savings fetched successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/month-stats/user")
    public ResponseEntity<APIResponse<MonthStatsResponse>> getMonthStats(@RequestParam("month") Integer month,@RequestParam("year") Integer year){
        Integer userId= AuthenticationUtil.getCurrentUserId();
        MonthStatsResponse response =
                analyticsService.getMonthStats(userId,month,year);

        APIResponse<MonthStatsResponse> apiResponse=new APIResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setData(response);
        apiResponse.setMessage("Monthly savings fetched successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/monthly-needs-wants/user")
    public ResponseEntity<APIResponse<List<MonthlyNeedsWantsResponse>>> getMonthNeedsWants(@RequestParam("year") Integer year){
        Integer userId= AuthenticationUtil.getCurrentUserId();
        List<MonthlyNeedsWantsResponse> response =
                analyticsService.getMonthlyNeedsWants(userId,year);

        APIResponse<List<MonthlyNeedsWantsResponse>> apiResponse=new APIResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setData(response);
        apiResponse.setMessage("Monthly needs/wants fetched successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/monthly-category/user")
    public ResponseEntity<APIResponse<List<MonthlyCategoryResponse>>> getCategoryWise(@RequestParam("year") Integer year,@RequestParam("month") Integer month){
        Integer userId= AuthenticationUtil.getCurrentUserId();
        List<MonthlyCategoryResponse> response =
                analyticsService.getCategoryWise(userId,year,month);

        APIResponse<List<MonthlyCategoryResponse>> apiResponse=new APIResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setData(response);
        apiResponse.setMessage("Monthly category wise fetched successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/income-vs-expense/user")
    public ResponseEntity<APIResponse<List<MonthlyIncomeExpenseReponse>>> getMonthlyIncomeExpense(@RequestParam("year") Integer year){
        Integer userId= AuthenticationUtil.getCurrentUserId();
        List<MonthlyIncomeExpenseReponse> response =
                analyticsService.getMonthlyIncomeVsExpense(userId,year);

        APIResponse<List<MonthlyIncomeExpenseReponse>> apiResponse=new APIResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setData(response);
        apiResponse.setMessage("Monthly income vs expense fetched successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }




}
