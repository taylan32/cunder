package com.example.cunder.exception;

public class BannedUserLoginException extends RuntimeException{
    public BannedUserLoginException(String message) {
        super(message);
    }
}
