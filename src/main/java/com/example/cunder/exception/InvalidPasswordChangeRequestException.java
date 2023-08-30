package com.example.cunder.exception;

public class InvalidPasswordChangeRequestException extends RuntimeException{
    public InvalidPasswordChangeRequestException(String message) {
        super(message);
    }
}
