package org.example.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.enums.BillRecurrence;
import org.example.enums.BillStatus;
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
@Table(name = "bills")
@Check(constraints = "amount > 0")
public class Bill {

    @Id
    @Column(name = "bill_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "bill_seq_gen")
    @SequenceGenerator(name="bill_seq_gen",sequenceName = "bill_seq",allocationSize = 25)
    private Integer billId;

    @ManyToOne
    @NotNull
    @JoinColumn(referencedColumnName = "user_id",name = "user_id",nullable = false)
    private User user;

    @Column(name = "title",nullable = false)
    @NotBlank
    private String title;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "recurrence",nullable = false)
    private BillRecurrence billRecurrence;

    @NonNull
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @NonNull
    @Column(name = "due_date",nullable = false)
    private LocalDate latestDueDate;


    @CreationTimestamp
    //Automatically sets the value when the value is being generated. That is when the user is being registered
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
}
