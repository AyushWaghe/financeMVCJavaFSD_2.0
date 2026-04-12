package org.example.dto;

import lombok.Data;

@Data
public class APIResponse <T>{
    private boolean success;
    private String message;
    private T data;
}
