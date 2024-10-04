package com.prgrms2.java.bitta.apply.controller.advice;

import com.prgrms2.java.bitta.apply.exception.ApplyTaskException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApplyControllerAdvice {

    @ExceptionHandler(ApplyTaskException.class)
    public ResponseEntity<Map<String, String>> handleApplyTaskException(ApplyTaskException e) {
        return ResponseEntity.status(e.getCode())
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
    }
}

