package org.example.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.SpendingType;
import org.example.enums.TransactionType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(name = "transaction_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "tran_seq_gen")
    @SequenceGenerator(name="tran_seq_gen",sequenceName = "tran_seq",allocationSize = 50)
    private Integer Id;

    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName = "user_id")
    private User user;

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="amount")
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name="category_id",referencedColumnName ="category_id")
    private Category category;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name="transaction_type")
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "spending_type")
    private SpendingType spendingType;

    @CreationTimestamp
    //Automatically sets the value when the value is being generated. That is when the user is being registered
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

}
