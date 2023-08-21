package com.example.cunder.exception;

public class UnverifiedUserLoginException extends RuntimeException{
    public UnverifiedUserLoginException(String message) {
        super(message);
    }
}
