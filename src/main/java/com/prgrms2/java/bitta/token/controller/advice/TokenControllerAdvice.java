package com.prgrms2.java.bitta.token.controller.advice;

import com.prgrms2.java.bitta.token.exception.TokenTaskException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class TokenControllerAdvice {
    @ExceptionHandler(TokenTaskException.class)
    public ResponseEntity<?> handleArgsException(TokenTaskException e) {
        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}