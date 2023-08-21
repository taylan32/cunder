package com.example.cunder.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> fields = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(e -> {
            fields.put(((FieldError)e).getField(), e.getDefaultMessage());
        });
        Map<String, Object> errors = new HashMap<>();
        errors.put("timestamp", new Date().toString());
        errors.put("title", "Validation error(s)");
        errors.put("details", fields);
        errors.put("message", "Validation rules have been violated");
        errors.put("status", HttpStatus.BAD_REQUEST.toString());
        return null;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now().toString());
        error.put("title", "Not Found");
        error.put("message", exception.getMessage());
        error.put("status", HttpStatus.NOT_FOUND.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyExistsException(AlreadyExistsException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now().toString());
        error.put("title", "Already Exists");
        error.put("message", exception.getMessage());
        error.put("status", HttpStatus.CONFLICT.toString());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(BannedUserLoginException.class)
    public ResponseEntity<Object> handleBannedUserLoginException(BannedUserLoginException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now().toString());
        error.put("title", "Banned user");
        error.put("message", exception.getMessage());
        error.put("status", HttpStatus.FORBIDDEN.toString());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(UnverifiedUserLoginException.class)
    public ResponseEntity<Object> handleUnverifiedUserLoginException(UnverifiedUserLoginException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now().toString());
        error.put("title", "Unverified user");
        error.put("message", exception.getMessage());
        error.put("status", HttpStatus.FORBIDDEN.toString());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

}
