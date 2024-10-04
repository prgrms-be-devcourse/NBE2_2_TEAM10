package com.prgrms2.java.bitta.jobpost.controller.advice;

import com.prgrms2.java.bitta.jobpost.exception.JobPostTaskException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class JobPostControllerAdvice {
    @ExceptionHandler(JobPostTaskException.class)
    public ResponseEntity<Map<String, String>> handleArgsException(JobPostTaskException e) {
        return ResponseEntity.status(e.getCode())
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleArgsException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleArgsException(ConstraintViolationException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
    }
}
