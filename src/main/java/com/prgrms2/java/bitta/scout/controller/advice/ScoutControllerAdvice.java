package com.prgrms2.java.bitta.scout.controller.advice;

import com.prgrms2.java.bitta.scout.exception.ScoutTaskException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ScoutControllerAdvice {

    @ExceptionHandler(ScoutTaskException.class)
    public ResponseEntity<?> handleScoutException(ScoutTaskException e) {
        return ResponseEntity.status(e.getCode())
                .body(Map.of("error", e.getMessage()));
    }
}