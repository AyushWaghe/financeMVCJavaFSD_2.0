package org.example.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.BillStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Data
@Table(name = "bill_instances")
public class BillInstance {

    @Id
    @Column(name = "bill_instance_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "bill_seq_gen")
    @SequenceGenerator(name="bill_seq_gen",sequenceName = "bill_seq",allocationSize = 25)
    private Integer billInstanceId;

    @ManyToOne
    @JoinColumn(name = "bill_id",referencedColumnName = "bill_id")
    private Bill bill;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private User user;

    @NotBlank
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "amount")
    private BigDecimal amount;

    @NotNull
    @Column(name = "due_date")
    private LocalDate dueDate;

    @NotNull
    @Enumerated
    @Column(name = "status")
    BillStatus billStatus;

}
