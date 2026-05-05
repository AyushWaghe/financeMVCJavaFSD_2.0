package org.example.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.example.enums.BillRecurrence;
import org.example.enums.BillStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "bills")
public class Bill {

    @Id
    @Column(name = "BillId")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "bill_seq_gen")
    @SequenceGenerator(name="bill_seq_gen",sequenceName = "bill_seq",allocationSize = 25)
    private Integer billId;

    @ManyToOne
    @NotNull
    @JoinColumn(referencedColumnName = "user_id",name = "user_id")
    private User user;

    @Column(name = "title")
    @NotBlank
    private String billTitle;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "recurrence")
    private BillRecurrence billRecurrence;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "bill_status")
    private BillStatus billStatus;

    @NonNull
    @Column(name = "amount")
    private BigDecimal amount;

    @NonNull
    @Column(name = "due_date")
    private LocalDate dueDate;


    @CreationTimestamp
    //Automatically sets the value when the value is being generated. That is when the user is being registered
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
}
