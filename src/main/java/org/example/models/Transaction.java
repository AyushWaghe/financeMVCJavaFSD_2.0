package org.example.models;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.SpendingType;
import org.example.enums.TransactionType;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(name = "transaction_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "tran_seq_gen")
    @SequenceGenerator(name="tran_seq_gen",sequenceName = "tran_seq",allocationSize = 50)
    private Integer Id;

    @ManyToOne
    @NonNull
    @JoinColumn(name="user_id",referencedColumnName = "user_id")
    private User user;

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="amount",precision = 8,scale = 2,nullable = false)
    @NonNull
    @Check(constraints = "amount > 0")
    private BigDecimal amount;

    @ManyToOne
//    @NonNull
    @JoinColumn(name="category_id",referencedColumnName ="category_id")
    private Category category;

    @Column(name = "transaction_date")
    @NonNull
    private LocalDate transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name="transaction_type")
    @NonNull
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "spending_type")
    @NonNull
    private SpendingType spendingType;

    @CreationTimestamp
    //Automatically sets the value when the value is being generated. That is when the user is being registered
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    //Copy constructer
    public Transaction(Transaction other) {
        this.Id = other.Id;
        this.title = other.title;
        this.description = other.description;
        this.amount = other.amount;
        this.category = other.category;
        this.transactionDate = other.transactionDate;
        this.type = other.type;
        this.spendingType = other.spendingType;
        this.user = other.user;
    }

}
