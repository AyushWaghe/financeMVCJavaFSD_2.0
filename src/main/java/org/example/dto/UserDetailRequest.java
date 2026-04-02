package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDetailRequest {

    @NotBlank(message = "User id cannot be null")
    private Integer id;

    private Integer needs;

    private Integer wants;

    private Integer savings;

    private BigDecimal total_bal;

    private String address;

    private String name;

    public UserDetailRequest(Integer id, Integer needs, Integer wants, Integer savings, BigDecimal total_bal, String address, String name) {
        this.id = id;
        this.needs = needs;
        this.wants = wants;
        this.savings = savings;
        this.total_bal = total_bal;
        this.address = address;
        this.name = name;
    }
}
