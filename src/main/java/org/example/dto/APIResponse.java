package org.example.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL) //This will not send null fields in the response JSON
@Data
public class APIResponse <T>{
    private boolean success;
    private String message;
    private T data;
}
