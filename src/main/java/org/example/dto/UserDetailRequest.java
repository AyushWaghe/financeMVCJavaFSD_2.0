package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDetailRequest {

//    @NotBlank(message = "User id cannot be null")---> This is for strings only for Integer follow the below

    private Integer needs;

    private Integer wants;

    private Integer savings;

    private String address;

    private String name;

    private boolean notificationSubscribed;

    public UserDetailRequest(Integer needs, Integer wants, Integer savings, BigDecimal total_bal, String address, String name,boolean notificationSubscribed) {
        this.needs = needs;
        this.wants = wants;
        this.savings = savings;
        this.address = address;
        this.name = name;
        this.notificationSubscribed=notificationSubscribed;
    }
}
