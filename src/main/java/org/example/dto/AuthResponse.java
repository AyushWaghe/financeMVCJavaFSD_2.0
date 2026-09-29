package org.example.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private boolean success;
    private Integer userId;
    private String useremail;
    private String message;

    public AuthResponse(boolean success, Integer userId, String useremail,String message) {
        this.success = success;
        this.userId = userId;
        this.useremail = useremail;
        this.message=message;
    }
}
