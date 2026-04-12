package org.example.exceptions;

import org.example.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


    //Custom exceptions-----------------------------------------------------------------------------------------------
    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ErrorResponse> userExistsException(UserExistsException e){
        ErrorResponse err=new ErrorResponse(409,"User already exists","CONFLICT");
        return new ResponseEntity<>(err, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserDetailNotFoundException.class)
    public ResponseEntity<ErrorResponse> userDetailNotFound(UserDetailNotFoundException e){
        ErrorResponse err=new ErrorResponse(404,"User details not found","NOT_FOUND");
        return new ResponseEntity<>(err,HttpStatus.NOT_FOUND);
    }

    //General exception---------------------------------------------------------------------------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> generalException(Exception e){
        System.out.println(e);
        ErrorResponse err=new ErrorResponse(
                500,
                "Something went wrong",
                "INTERNAL_SERVER_ERROR"
        );
        return new ResponseEntity<>(err,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Method exceptions-------------------------------------------------------------------------------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> invalidMethodArgument(MethodArgumentNotValidException e){
        ErrorResponse err=new ErrorResponse(
               400,
                "Invalid method arguments",
                "BAD_REQUEST"
        );
        Map<String,String > errors=new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        err.setFieldErrors(errors);

        return new ResponseEntity<>(err,HttpStatus.BAD_REQUEST);
    }

}
