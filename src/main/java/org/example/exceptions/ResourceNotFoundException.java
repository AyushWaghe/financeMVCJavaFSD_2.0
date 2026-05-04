package org.example.exceptions;

public class ResourceNotFoundException  extends RuntimeException{
    public ResourceNotFoundException(String msg){
        super(msg);
    }
}
