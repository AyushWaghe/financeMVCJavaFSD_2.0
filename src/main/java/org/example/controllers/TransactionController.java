package org.example.controllers;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.example.dto.APIResponse;
import org.example.dto.TransactionRequest;
import org.example.dto.TransactionResponse;
import org.example.models.Transaction;
import org.example.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping()
    public ResponseEntity<APIResponse<Void>> saveTransaction(@Valid @RequestBody TransactionRequest transactionRequest){
        Transaction tr=transactionService.saveTransaction(transactionRequest);
        APIResponse<Void> apiResponse=new APIResponse();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Transaction saved successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<APIResponse<List<TransactionResponse>>> getTransactions(@RequestParam Integer userId, @RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate){
        List<TransactionResponse> transactionResponses=transactionService.getTransactions(userId,startDate,endDate);
        APIResponse<List<TransactionResponse>> apiResponse=new APIResponse<>();
        apiResponse.setData(transactionResponses);
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Transactions fetched successfully");
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> updateTransaction(@Valid @RequestBody TransactionRequest transactionRequest,@PathVariable("id") Integer id){
        transactionService.updateTransaction(transactionRequest,id);
        APIResponse<Void> apiResponse=new APIResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Transaction updated successfully");
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @DeleteMapping("/{tid}")
    public ResponseEntity<APIResponse<Void>> deleteTransaction(@PathVariable("tid") Integer tId){
        transactionService.deleteTransaction(tId);
        APIResponse<Void> apiResponse=new APIResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Transaction delete successfully");
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
}
