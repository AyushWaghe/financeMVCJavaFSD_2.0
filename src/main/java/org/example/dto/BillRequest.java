package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.example.enums.BillRecurrence;
import org.example.enums.BillStatus;
import org.example.models.Bill;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BillRequest {

    @NotNull
    private Integer userId;

    @NotBlank
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotNull
    @DecimalMin(value = "0.0",inclusive = false,message = "Bill amount must be greater than 0")
    @DecimalMax(value = "10000000.0",inclusive = false,message = "Bill amount must be less that 1cr")
    private BigDecimal amount;

    @NotNull
    @FutureOrPresent(message = "Due date cannot be in the past")
    private LocalDate dueDate;

//    @NotNull
    private BillRecurrence billRecurrence;

    private Integer billId; //Template id of the bill
}
