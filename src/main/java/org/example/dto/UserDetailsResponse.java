package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDetailsResponse {
    private Integer needs;

    private Integer wants;

    private Integer savings;

    private String address;

    private String name;

    private boolean notificationSubscribed;

    public UserDetailsResponse(Integer needs, Integer wants, Integer savings, String address, String name,boolean notificationSubscribed) {
        this.needs = needs;
        this.wants = wants;
        this.savings = savings;
        this.address = address;
        this.name=name;
        this.notificationSubscribed=notificationSubscribed;
    }
}
