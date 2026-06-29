package org.example.dao;

import jakarta.transaction.Transactional;
import org.example.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    List<Transaction> findByUser_UserIdAndTransactionDateBetween(Integer userId, LocalDate startDate, LocalDate endDate);

    List<Transaction> findByUser_UserId(Integer userId);

    @Modifying //Here we tell spring that this is delete query something that modifies the DB since by default all queries are read queries in spring
    @Transactional
    /*
    You need @Transactional because your delete query must run inside a database transaction—otherwise Spring/JPA cannot guarantee execution or commit.
     Start a DB transaction → run this method → commit if success → rollback if failure
     */
    @Query("DELETE FROM Transaction t WHERE t.id= :id")
    int deleteTransactionById(Integer id);
}
