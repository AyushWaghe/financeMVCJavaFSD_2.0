package org.example.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private int statusCode;
    private String message;
    private String error;
    private LocalDateTime timestamp;

    public ErrorResponse(int statusCode, String message, String error) {
        this.statusCode = statusCode;
        this.message = message;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }
}
