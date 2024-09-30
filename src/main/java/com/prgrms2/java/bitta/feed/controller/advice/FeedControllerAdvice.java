package com.prgrms2.java.bitta.feed.controller.advice;

import com.prgrms2.java.bitta.feed.exception.FeedException;
import com.prgrms2.java.bitta.feed.exception.FeedTaskException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class FeedControllerAdvice {
    @ExceptionHandler(FeedTaskException.class)
    public ResponseEntity<?> handleArgsException(FeedTaskException e) {
        return ResponseEntity.status(e.getCode())
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleArgsException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(Map.of("error", "잘못된 요청입니다.", "reason", e.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleArgsException(ConstraintViolationException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", "잘못된 요청입니다.", "reason", "ID는 음수가 될 수 없습니다."));
    }
}
