package org.example.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.example.enums.BillRecurrence;
import org.example.enums.BillStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class BillResponse {

    @NotBlank
    private String title;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    private BillRecurrence billRecurrence;

    private BillStatus billStatus;
}
