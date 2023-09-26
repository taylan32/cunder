package com.example.cunder.exception;

public class MathRequestLimitExceedException extends RuntimeException{

    public MathRequestLimitExceedException(String message) {
        super(message);
    }
}
