package org.example.dao;

import org.example.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnalyticsRepository extends JpaRepository<Transaction,Integer> {

    //Monthly expense spending
    @Query(value = """
    WITH months AS (
        SELECT generate_series(1, 12) AS month_num
    )

    SELECT 
        TO_CHAR(TO_DATE(months.month_num::text, 'MM'), 'Mon') AS month,

        COALESCE(SUM(t.amount), 0) AS total_spending

    FROM months

    LEFT JOIN transactions t
        ON EXTRACT(MONTH FROM t.transaction_date) = months.month_num
        AND EXTRACT(YEAR FROM t.transaction_date) = :year
        AND t.user_id = :userId
        AND t.transaction_type = 'EXPENSE'

    GROUP BY months.month_num

    ORDER BY months.month_num
""", nativeQuery = true)
    List<Object[]> getMonthlyExpenses(
            @Param("userId") Integer userId,
            @Param("year") int year
    );


    //Monthly needs/wants spendings
    @Query(value = """
    WITH months AS (
        SELECT 
            generate_series(1, 12) AS month_num
    )

    SELECT 
        TO_CHAR(TO_DATE(months.month_num::text, 'MM'), 'Mon') AS month,

        COALESCE(SUM(
            CASE 
                WHEN t.spending_type = 'NEEDS' THEN t.amount
                ELSE 0
            END
        ), 0) AS needs,

        COALESCE(SUM(
            CASE 
                WHEN t.spending_type = 'WANTS' THEN t.amount
                ELSE 0
            END
        ), 0) AS wants

    FROM months

    LEFT JOIN transactions t
        ON EXTRACT(MONTH FROM t.transaction_date) = months.month_num
        AND EXTRACT(YEAR FROM t.transaction_date) = :year
        AND t.user_id = :userId
        AND t.transaction_type = 'EXPENSE'

    GROUP BY months.month_num

    ORDER BY months.month_num
""", nativeQuery = true)
    List<Object[]> getMonthlyNeedsWants(
            @Param("userId") Integer userId,
            @Param("year") int year
    );

    //Category wise monthly spendings
    @Query(value = """
    SELECT 
        c.title AS category,
        COALESCE(SUM(t.amount), 0) AS total_spending

    FROM transactions t

    JOIN categories c
        ON t.category_id = c.category_id

    WHERE t.user_id = :userId
    AND t.transaction_type = 'EXPENSE'
    AND EXTRACT(YEAR FROM t.transaction_date) = :year
    AND EXTRACT(MONTH FROM t.transaction_date) = :month

    GROUP BY c.category_id, c.title

    ORDER BY total_spending DESC
""", nativeQuery = true)
    List<Object[]> getCategoryWiseSpending(
            @Param("userId") Integer userId,
            @Param("year") int year,
            @Param("month") int month
    );

    //Get monthly income vs expense spendings
    @Query(value = """
    WITH months AS (
        SELECT generate_series(1, 12) AS month_num
    )

    SELECT 
        TO_CHAR(TO_DATE(months.month_num::text, 'MM'), 'Mon') AS month,

        COALESCE(SUM(
            CASE 
                WHEN t.transaction_type = 'INCOME' THEN t.amount
                ELSE 0
            END
        ), 0) AS income,

        COALESCE(SUM(
            CASE 
                WHEN t.transaction_type = 'EXPENSE' THEN t.amount
                ELSE 0
            END
        ), 0) AS expense

    FROM months

    LEFT JOIN transactions t
        ON EXTRACT(MONTH FROM t.transaction_date) = months.month_num
        AND EXTRACT(YEAR FROM t.transaction_date) = :year
        AND t.user_id = :userId

    GROUP BY months.month_num

    ORDER BY months.month_num
""", nativeQuery = true)
    List<Object[]> getMonthlyIncomeVsExpense(
            @Param("userId") Integer userId,
            @Param("year") int year
    );

    //Monthly top 3 spendings category wise
    @Query(value = """
    WITH ranked_categories AS (

        SELECT 
            EXTRACT(MONTH FROM t.transaction_date) AS month_num,
            TO_CHAR(t.transaction_date, 'Mon') AS month,
            c.title AS category,
            SUM(t.amount) AS total_spending,

            ROW_NUMBER() OVER (
                PARTITION BY EXTRACT(MONTH FROM t.transaction_date)
                ORDER BY SUM(t.amount) DESC
            ) AS rank

        FROM transactions t

        JOIN categories c
            ON t.category_id = c.category_id

        WHERE t.user_id = :userId
        AND t.transaction_type = 'EXPENSE'
        AND EXTRACT(YEAR FROM t.transaction_date) = :year

        GROUP BY 
            EXTRACT(MONTH FROM t.transaction_date),
            TO_CHAR(t.transaction_date, 'Mon'),
            c.category_id,
            c.title
    )

    SELECT 
        month,
        category,
        total_spending

    FROM ranked_categories

    WHERE rank <= 3

    ORDER BY month_num, total_spending DESC
""", nativeQuery = true)
    List<Object[]> getMonthlyTop3Categories(
            @Param("userId") Integer userId,
            @Param("year") int year
    );
}
