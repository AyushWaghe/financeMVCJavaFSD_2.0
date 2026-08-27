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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);


    // Custom exceptions ---------------------------------------------------------------------------------------------

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ErrorResponse> userExistsException(
            UserExistsException e) {

        log.error("User already exists", e);

        ErrorResponse err =
                new ErrorResponse(
                        409,
                        "User already exists",
                        "CONFLICT"
                );

        return new ResponseEntity<>(err, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFound(
            ResourceNotFoundException e) {

        log.error("Resource not found: {}", e.getMessage(), e);

        ErrorResponse err =
                new ErrorResponse(
                        404,
                        e.getMessage(),
                        "NOT_FOUND"
                );

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> transactionNotFound(
            TransactionNotFoundException e) {

        log.error("Transaction not found: {}", e.getMessage(), e);

        ErrorResponse err =
                new ErrorResponse(
                        404,
                        e.getMessage(),
                        "NOT_FOUND"
                );

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(FinanceAIClientException.class)
    public ResponseEntity<ErrorResponse> financeAIClientException(
            FinanceAIClientException e) {

        log.error("Finance-AI client request failed", e);

        ErrorResponse err =
                new ErrorResponse(
                        500,
                        e.getMessage(),
                        "INTERNAL_SERVER_ERROR"
                );

        return new ResponseEntity<>(
                err,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


    // Built-in exceptions -------------------------------------------------------------------------------------------

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> nullPointerExp(
            NullPointerException e) {

        log.error("Null pointer exception", e);

        ErrorResponse err =
                new ErrorResponse(
                        400,
                        "Invalid request",
                        "BAD_REQUEST"
                );

        return new ResponseEntity<>(
                err,
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> httpMessageNotReadable(
            HttpMessageNotReadableException e) {

        log.error("Malformed request body", e);

        String message = "Invalid value provided for enum field";

        if (e.getCause() != null &&
                e.getCause().getMessage() != null) {

            String causeMessage = e.getCause().getMessage();

            if (causeMessage.contains("TransactionType")) {
                message =
                        "Invalid transaction type. Allowed values: EXPENSE, INCOME";
            }

            if (causeMessage.contains("SpendingType")) {
                message =
                        "Invalid transaction type. Allowed values: NEEDS, WANTS, SAVINGS";
            }

            if (causeMessage.contains("LocalDateTime")) {
                message = "Invalid Date format";
            }

            if (causeMessage.contains("BillRecurrence")) {
                message =
                        "Invalid bill recurrence type. Allowed values: DAILY,WEEKLY,MONTHLY,QUATERLY,HALF-YEARLY,YEARLY";
            }

            if (causeMessage.contains("BillStatus")) {
                message =
                        "Invalid bill spending type. Allowed values: PENDING,OVERDUE,PAID";
            }
        }

        ErrorResponse err =
                new ErrorResponse(
                        400,
                        message,
                        "BAD_REQUEST"
                );

        return new ResponseEntity<>(
                err,
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgException(
            IllegalArgumentException e) {

        log.error("Illegal argument: {}", e.getMessage(), e);

        ErrorResponse err =
                new ErrorResponse(
                        400,
                        e.getMessage(),
                        "BAD_REQUEST"
                );

        return new ResponseEntity<>(
                err,
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ErrorResponse> dataNotFoundInDBException(
            EmptyResultDataAccessException e) {

        log.error("Database record not found", e);

        ErrorResponse err =
                new ErrorResponse(
                        404,
                        e.getMessage(),
                        "NOT_FOUND"
                );

        return new ResponseEntity<>(
                err,
                HttpStatus.NOT_FOUND
        );
    }


    @ExceptionHandler(UserDetailNotFoundException.class)
    public ResponseEntity<ErrorResponse> userDetailNotFound(
            UserDetailNotFoundException e) {

        log.error("User details not found", e);

        ErrorResponse err =
                new ErrorResponse(
                        404,
                        "User details not found",
                        "NOT_FOUND"
                );

        return new ResponseEntity<>(
                err,
                HttpStatus.NOT_FOUND
        );
    }


    // General exception ---------------------------------------------------------------------------------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> generalException(
            Exception e) {

        log.error("Unhandled exception occurred", e);

        ErrorResponse err =
                new ErrorResponse(
                        500,
                        "Something went wrong",
                        "INTERNAL_SERVER_ERROR"
                );

        return new ResponseEntity<>(
                err,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


    // Method exceptions ---------------------------------------------------------------------------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> invalidMethodArgument(
            MethodArgumentNotValidException e) {

        log.error("Method argument validation failed", e);

        ErrorResponse err =
                new ErrorResponse(
                        400,
                        "Invalid method arguments",
                        "BAD_REQUEST"
                );

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        err.setFieldErrors(errors);

        return new ResponseEntity<>(
                err,
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> methodArgMismatch(
            MethodArgumentTypeMismatchException e) {

        log.error(
                "Method argument type mismatch: {}",
                e.getMessage(),
                e
        );

        ErrorResponse errorResponse =
                new ErrorResponse(
                        400,
                        e.getMessage(),
                        "BAD_REQUEST"
                );

        if (e.getRequiredType() == LocalDate.class) {

            errorResponse.setStatusCode(400);
            errorResponse.setMessage(
                    "Invalid date format. Expected yyyy-mm-dd"
            );
            errorResponse.setError("BAD_REQUEST");
        }

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.BAD_REQUEST
        );
    }
}