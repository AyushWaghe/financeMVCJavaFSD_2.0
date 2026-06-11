package org.example.exceptions;

import org.example.dto.ErrorResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
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

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFound(ResourceNotFoundException e){
        ErrorResponse err=new ErrorResponse(404,e.getMessage(),"NOT_FOUND");
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> transactionNotFound(TransactionNotFoundException e){
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
        if(e.getCause()!=null && e.getCause().getMessage().contains("BillRecurrence")) {
            message = "Invalid bill recurrence type. Allowed values: DAILY,WEEKLY,MONTHLY,QUATERLY,HALF-YEARLY,YEARLY";
        }
        if(e.getCause()!=null && e.getCause().getMessage().contains("BillStatus")) {
            message = "Invalid bill spending type. Allowed values: PENDING,OVERDUE,PAID";
        }

        ErrorResponse err=new ErrorResponse(409,message,"CONFLICT");
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgException(IllegalArgumentException e){
        ErrorResponse err=new ErrorResponse(409,e.getMessage(),"BAD_REQUEST");
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ErrorResponse> dataNotFoundInDBException(EmptyResultDataAccessException e) {
        ErrorResponse err = new ErrorResponse(404, e.getMessage(), "NOT_FOUND");
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
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

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> methodArgMismatch(MethodArgumentTypeMismatchException e){

        ErrorResponse errorResponse=new ErrorResponse(0,e.getMessage(),"BAD_REQUEST");

        if(e.getRequiredType()== LocalDate.class){
            errorResponse.setStatusCode(400);
            errorResponse.setMessage("Invalid date format. Expected yyyy-mm-dd");
            errorResponse.setError("BAD_REQUEST");
        }

        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

}
