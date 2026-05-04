package org.example.dao;

import org.example.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

    @Query("""
            SELECT t FROM Transaction t JOIN FETCH t.category WHERE t.user.userId = :userId AND t.transactionDate BETWEEN :startDate AND :endDate
            """)
    List<Transaction> findByUser_UserIdAndTransactionDateBetween(Integer userId, LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findByUser_UserId(Integer userId);
}
