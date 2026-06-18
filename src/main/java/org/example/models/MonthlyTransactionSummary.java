package org.example.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        name = "monthly_transaction_summary",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_year_month",
                        columnNames = {"user_id", "year", "month"}
                )
        }
)
public class MonthlyTransactionSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bud_gen")
    @SequenceGenerator(
            name = "bud_gen",
            sequenceName = "bud_gen_seq",
            allocationSize = 25
    )
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "month", nullable = false)
    private Integer month;

    // Aggregates---------------------------------------------------------

    @Column(name = "total_income", precision = 19, scale = 2)
    private BigDecimal totalIncome = BigDecimal.ZERO;

    @Column(name = "total_expense", precision = 19, scale = 2)
    private BigDecimal totalExpense = BigDecimal.ZERO;

    @Column(name = "total_need_expense", precision = 19, scale = 2)
    private BigDecimal totalNeedExpense = BigDecimal.ZERO;

    @Column(name = "total_want_expense", precision = 19, scale = 2)
    private BigDecimal totalWantExpense = BigDecimal.ZERO;

    @Column(name = "total_savings", precision = 19, scale = 2)
    private BigDecimal totalSavings = BigDecimal.ZERO;

    //Notification 0 -> NONE, 100 -> Notification send ---------------------------

    @Column(name = "last_need_threshold_sent", nullable = false)
    private Integer lastNeedThresholdSent = 0;

    @Column(name = "last_want_threshold_sent", nullable = false)
    private Integer lastWantThresholdSent = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}