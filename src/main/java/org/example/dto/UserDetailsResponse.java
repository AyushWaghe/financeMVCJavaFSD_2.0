package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDetailsResponse {
    private Integer needs;

    private Integer wants;

    private Integer savings;

    private BigDecimal total_bal;

    private String address;

    private String username;

    public UserDetailsResponse(Integer needs, Integer wants, Integer savings, BigDecimal total_bal, String address, String username) {
        this.needs = needs;
        this.wants = wants;
        this.savings = savings;
        this.total_bal = total_bal;
        this.address = address;
        this.username=username;
    }
}
