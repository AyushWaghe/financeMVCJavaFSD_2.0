package org.example.services;

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
import org.springframework.transaction.annotation.Transactional;

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

        User user = userRepository.getReferenceById(transactionRequest.getUserId());

        List<Category> userCategories=categoryService.getCategories(transactionRequest.getUserId());
        Category category=null;
        for(Category c:userCategories){
            if(transactionRequest.getCategory().equals(c.getTitle())){
                category=c;
                break;
            }
        }
        if(category==null) {    //User category not found create one
            category=categoryService.createCategory(transactionRequest.getUserId(),transactionRequest.getCategory());
        }

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

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactions(Integer userId, LocalDate startDate,LocalDate endDate) {
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

        List<Transaction> transactions=transactionRepository.findByUser_UserIdAndTransactionDateBetween(userId,startDate,endDate);
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

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsMonthly(Integer userId, Integer month,Integer year) {

        List<Transaction> transactions=transactionRepository.getTransactionsByUserMonthAndYear(userId,month,year);
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

    @Transactional
    public void updateTransaction(TransactionRequest transactionRequest, Integer id) {
        Transaction transaction=transactionRepository.findById(id).orElseThrow(()->new TransactionNotFoundException("User with id "+transactionRequest.getUserId()+" not found"));
        Transaction oldTransaction=new Transaction(transaction);
        if(!transaction.getUser().getUserId().equals(transactionRequest.getUserId())){
            throw new IllegalArgumentException("User id from transaction "+transaction.getUser().getUserId()+" and transaction request "+transactionRequest.getUserId()+ " did not match");
        }

        List<Category> userCategories=categoryService.getCategories(transaction.getUser().getUserId());
        Category category=null;
        for(Category c:userCategories){
            if(transactionRequest.getCategory().equals(c.getTitle())){
                category=c;
                break;
            }
        }
        if(category==null) {    //User category not found create one
            category=categoryService.createCategory(transactionRequest.getUserId(),transactionRequest.getCategory());
        }

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

    @Transactional
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
