package com.prgrms2.java.bitta.member.controller.advice;

import com.prgrms2.java.bitta.member.exception.MemberTaskException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;


@Slf4j
@RestControllerAdvice
public class MemberControllerAdvice {
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> handleArgsException(AuthorizationDeniedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "해당 리소스에 대한 권한이 없습니다."));
    }

    @ExceptionHandler(MemberTaskException.class)
    public ResponseEntity<?> handleArgsException(MemberTaskException e) {
        return ResponseEntity.status(e.getCode())
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleArgsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "로그인에 실패했습니다."));
    }
}

