package com.prgrms2.java.bitta.member.controller.advice;

import com.prgrms2.java.bitta.member.exception.MemberTaskException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestControllerAdvice
public class TokenControllerAdvice {

    @ExceptionHandler({AuthorizationDeniedException.class, MemberTaskException.class})
    public ResponseEntity<?> handleException(RuntimeException e) {
        Map<String, Object> errMap = new HashMap<>();
        errMap.put("message", e.getMessage());

        HttpStatus status = (e instanceof MemberTaskException) ?
                HttpStatus.valueOf(((MemberTaskException) e).getCode()) : HttpStatus.FORBIDDEN;

        log.error("예외 발생: {}", e.getMessage());

        return ResponseEntity.status(status).body(errMap);
    }
}

