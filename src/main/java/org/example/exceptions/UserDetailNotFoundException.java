package org.example.exceptions;

public class UserDetailNotFoundException extends RuntimeException{
    public UserDetailNotFoundException(String message){
        super(message);
    }
}
