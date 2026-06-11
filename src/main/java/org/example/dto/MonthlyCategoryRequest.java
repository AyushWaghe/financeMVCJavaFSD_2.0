package org.example.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Year;

@Data
@AllArgsConstructor
public class MonthlyCategoryRequest {
    @NotNull(message = "User id cannot be null")
    private Integer userId;

    @Min(value = 2000, message = "Year must be valid")
    @Max(value = 2100, message = "Year must be valid")
    private Integer year= Year.now().getValue();

    @NotNull
    private Integer month;
}
