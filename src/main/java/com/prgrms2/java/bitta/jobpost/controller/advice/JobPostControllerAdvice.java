package com.prgrms2.java.bitta.jobpost.controller.advice;

import com.prgrms2.java.bitta.jobpost.exception.JobPostTaskException;
import org.springframework.http.ResponseEntity;
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
}
