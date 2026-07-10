package org.example.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dao.CategoryRepository;
import org.example.dao.MonthlyTransactionSummaryRepository;
import org.example.dao.TransactionRepository;
import org.example.dao.UserRepository;
import org.example.dto.TransactionRequest;
import org.example.dto.TransactionResponse;
import org.example.exceptions.ResourceNotFoundException;
import org.example.exceptions.TransactionNotFoundException;
import org.example.exceptions.UserDetailNotFoundException;
import org.example.models.Category;
import org.example.models.Transaction;
import org.example.models.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {


    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final MonthlyTransactionSummaryService monthlyTransactionSummaryService;

    @Transactional
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

        Transaction savedTransaction=transactionRepository.save(transaction);

        monthlyTransactionSummaryService.updateMonthlySummaryOnTransactionCreate(savedTransaction);

        return savedTransaction;

    }

    public List<TransactionResponse> getTransactions(Integer userId, LocalDate startDate,LocalDate endDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDetailNotFoundException("User with id "+userId+" not found"));

        if(startDate!=null && endDate!=null && startDate.isAfter(endDate)){
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        LocalDate today=LocalDate.now();
        if (startDate == null) {
            startDate = today.withDayOfMonth(1);
        }

        if (endDate == null) {
            endDate = today;
        }

        List<Transaction> transactions=transactionRepository.findByUser_UserIdAndTransactionDateBetween(user.getUserId(),startDate,endDate);
        List<TransactionResponse> transactionList=transactions.stream().map(t-> new TransactionResponse(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.getAmount(),
                t.getCategory().getTitle(),
                t.getTransactionDate(),
                t.getType(),
                t.getSpendingType()
            )
        ).toList();

        return transactionList;

    }

    public List<TransactionResponse> getTransactionsMonthly(Integer userId, Integer month,Integer year) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDetailNotFoundException("User with id "+userId+" not found"));

        List<Transaction> transactions=transactionRepository.getTransactionsByUserMonthAndYear(user.getUserId(),month,year);
        List<TransactionResponse> transactionList=transactions.stream().map(t-> new TransactionResponse(
                        t.getId(),
                        t.getTitle(),
                        t.getDescription(),
                        t.getAmount(),
                        t.getCategory().getTitle(),
                        t.getTransactionDate(),
                        t.getType(),
                        t.getSpendingType()
                )
        ).toList();

        return transactionList;

    }

    public void updateTransaction(TransactionRequest transactionRequest, Integer id) {
        Transaction transaction=transactionRepository.findById(id).orElseThrow(()->new TransactionNotFoundException("User with id "+transactionRequest.getUserId()+" not found"));
        Transaction oldTransaction=new Transaction(transaction);
        if(!transaction.getUser().getUserId().equals(transactionRequest.getUserId())){
            throw new IllegalArgumentException("User id from transaction "+transaction.getUser().getUserId()+" and transaction request "+transactionRequest.getUserId()+ " did not match");
        }

        Category category=categoryService.findOrCreateCategory(transaction.getUser(),transactionRequest.getCategory());

        transaction.setTitle(transactionRequest.getTitle());
        transaction.setDescription(transactionRequest.getDescription());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setCategory(category);
        transaction.setTransactionDate(transactionRequest.getTransactionDate());
        transaction.setType(transactionRequest.getType());
        transaction.setSpendingType(transactionRequest.getSpendingType());

        Transaction updatedTransaction=transactionRepository.save(transaction);

        monthlyTransactionSummaryService.updateMonthlySummaryOnTransactionUpdate(oldTransaction,updatedTransaction);
    }

//    @Transactional
    public void deleteTransaction(Integer tId) {

        Transaction transaction = transactionRepository.findById(tId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Transaction with id " + tId + " not found"));

        monthlyTransactionSummaryService
                .updateMonthlySummaryOnTransactionDelete(transaction);

        transactionRepository.delete(transaction);
    }
}
