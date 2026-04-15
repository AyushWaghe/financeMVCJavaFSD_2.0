package org.example.controllers;

import jakarta.validation.Valid;
import org.example.dto.APIResponse;
import org.example.dto.TransactionRequest;
import org.example.models.Transaction;
import org.example.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping()
    public ResponseEntity<APIResponse<Void>> saveTransaction(@Valid @RequestBody TransactionRequest transactionRequest){
        Transaction tr=transactionService.saveTransaction(transactionRequest);
        APIResponse apiResponse=new APIResponse();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Transaction saved successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
}
