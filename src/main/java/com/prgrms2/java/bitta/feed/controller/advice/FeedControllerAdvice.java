package com.prgrms2.java.bitta.feed.controller.advice;

import com.prgrms2.java.bitta.feed.exception.FeedException;
import com.prgrms2.java.bitta.feed.exception.FeedTaskException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Map;

@RestControllerAdvice
public class FeedControllerAdvice {
    @ExceptionHandler(FeedTaskException.class)
    public ResponseEntity<?> handleArgsException(FeedTaskException e) {
        return ResponseEntity.status(e.getCode())
                .body(Map.of("error", e.getMessage()));
    }
}
