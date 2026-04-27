package org.example.dao;

import org.example.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

    List<Transaction> findByUser_UserIdAndTransactionDateBetween(Integer userId, LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findByUser_UserId(Integer userId);
}
