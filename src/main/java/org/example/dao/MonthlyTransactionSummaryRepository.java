package org.example.dao;

import org.example.models.MonthlyTransactionSummary;
import org.example.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface MonthlyTransactionSummaryRepository extends JpaRepository<MonthlyTransactionSummary,Integer> {
    Optional<MonthlyTransactionSummary> findByUserAndYearAndMonth(
            User user,
            Integer year,
            Integer month
    );

    @Modifying
    @Query("""
UPDATE MonthlyTransactionSummary m
SET m.totalIncome = m.totalIncome + :amount
WHERE m.user.userId = :userId
  AND m.year = :year
  AND m.month = :month
""")
    void updateIncome(
            @Param("userId") Integer userId,
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("amount") BigDecimal amount
    );

    @Modifying
    @Query("""
UPDATE MonthlyTransactionSummary m
SET m.totalExpense = m.totalExpense + :amount,
    m.totalNeedExpense = m.totalNeedExpense + :amount
WHERE m.user.userId = :userId
  AND m.year = :year
  AND m.month = :month
""")
    void updateNeedsExpense(
            @Param("userId") Integer userId,
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("amount") BigDecimal amount
    );

    @Modifying
    @Query("""
UPDATE MonthlyTransactionSummary m
SET m.totalExpense = m.totalExpense + :amount,
    m.totalWantExpense = m.totalWantExpense + :amount
WHERE m.user.userId = :userId
  AND m.year = :year
  AND m.month = :month
""")
    void updateWantsExpense(
            @Param("userId") Integer userId,
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("amount") BigDecimal amount
    );

    @Modifying
    @Query("""
UPDATE MonthlyTransactionSummary m
SET m.totalExpense = m.totalExpense + :amount,
    m.totalSavings = m.totalSavings + :amount
WHERE m.user.userId = :userId
  AND m.year = :year
  AND m.month = :month
""")
    void updateSavingsExpense(
            @Param("userId") Integer userId,
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("amount") BigDecimal amount
    );
}
