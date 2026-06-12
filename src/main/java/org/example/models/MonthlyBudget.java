package org.example.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "monthly_budgets")
public class MonthlyBudget {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "bug_gen")
    @SequenceGenerator(name = "bud_gen",sequenceName = "bud_gen_seq",allocationSize = 25)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_id",nullable = false)
    private User user;

    @Column(name = "month",nullable = false)
    private String month;

    @Column(name = "need_limit")
    private Integer monthlyNeedLimit;

    @Column(name = "want_limit")
    private Integer monthlyWantLimit;

    @Column(name = "saving_limit")
    private Integer monthlySavingLimit;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;


}
