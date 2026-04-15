package org.example.services;

import org.example.dao.TransactionRepository;
import org.example.dao.UserRepository;
import org.example.dto.TransactionRequest;
import org.example.exceptions.UserDetailNotFoundException;
import org.example.models.Transaction;
import org.example.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    public Transaction saveTransaction(TransactionRequest transactionRequest){

        if(transactionRequest.getAmount().compareTo(BigDecimal.ZERO)<=0) {
            throw new IllegalArgumentException("Transaction amount must be greater then 0");
        }

        User user = userRepository.findById(transactionRequest.getUserId())
                .orElseThrow(() -> new UserDetailNotFoundException("User with id "+transactionRequest.getUserId()+" not found"));
        Transaction transaction= Transaction.builder()
                .user(user)
                .title(transactionRequest.getTitle())
                .description(transactionRequest.getDescription())
                .amount(transactionRequest.getAmount())
                .category(transactionRequest.getCategory())
                .transactionDate(transactionRequest.getTransactionDate())
                .type(transactionRequest.getType())
                .spendingType(transactionRequest.getSpendingType())
                .build();

        return transactionRepository.save(transaction);

    }
}
