package org.example.exceptions;

import org.example.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
        ErrorResponse err=new ErrorResponse(404,e.getMessage(),"NOT_FOUND");
        return new ResponseEntity<>(err,HttpStatus.NOT_FOUND);
    }

    //In-build exception----------------------------------------------------------------------------------------------

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> nullPointerExp(NullPointerException e){
        ErrorResponse err=new ErrorResponse(409,"User already exists","CONFLICT");
        System.out.println(e);
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> nullPointerExp(HttpMessageNotReadableException e){

        String message="Invalid value provided for enum field";

        System.out.println(e);

        if(e.getCause()!=null && e.getCause().getMessage().contains("TransactionType")){
            message = "Invalid transaction type. Allowed values: EXPENSE, INCOME";
        }
        if(e.getCause()!=null && e.getCause().getMessage().contains("SpendingType")) {
            message = "Invalid transaction type. Allowed values: NEEDS, WANTS, SAVINGS";
        }
        if(e.getCause()!=null && e.getCause().getMessage().contains("LocalDateTime")) {
            message = "Invalid Date format";
        }

        ErrorResponse err=new ErrorResponse(409,message,"CONFLICT");
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgException(IllegalArgumentException e){
        ErrorResponse err=new ErrorResponse(409,e.getMessage(),"BAD_REQUEST");
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
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
