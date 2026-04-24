package org.example.services;

import org.example.dao.CategoryRepository;
import org.example.dao.TransactionRepository;
import org.example.dao.UserRepository;
import org.example.dto.TransactionRequest;
import org.example.exceptions.UserDetailNotFoundException;
import org.example.models.Category;
import org.example.models.Transaction;
import org.example.models.User;
import org.example.utils.CategoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

    public Transaction saveTransaction(TransactionRequest transactionRequest){

        User user = userRepository.findById(transactionRequest.getUserId())
                .orElseThrow(() -> new UserDetailNotFoundException("User with id "+transactionRequest.getUserId()+" not found"));

        Category category=categoryService.findOrCreateCategory(user,transactionRequest.getCategory());

        Transaction transaction= Transaction.builder()
                .user(user)
                .title(transactionRequest.getTitle())
                .description(transactionRequest.getDescription())
                .amount(transactionRequest.getAmount())
                .category(category)
                .transactionDate(transactionRequest.getTransactionDate())
                .type(transactionRequest.getType())
                .spendingType(transactionRequest.getSpendingType())
                .build();

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactions(Integer userId,String month) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDetailNotFoundException("User with id "+userId+" not found"));

        List<Transaction> transactions;
        
    }
}
