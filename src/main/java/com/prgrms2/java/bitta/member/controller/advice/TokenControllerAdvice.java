<<<<<<<< HEAD:src/main/java/com/prgrms2/java/bitta/user/controller/advice/TokenControllerAdvice.java
package com.prgrms2.java.bitta.user.controller.advice;

import com.prgrms2.java.bitta.user.exception.UserTaskException;
========
package com.prgrms2.java.bitta.member.controller.advice;

import com.prgrms2.java.bitta.member.exception.MemberTaskException;
>>>>>>>> refs/remotes/origin/main:src/main/java/com/prgrms2/java/bitta/member/controller/advice/TokenControllerAdvice.java
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class TokenControllerAdvice {
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> handleArgsException(AuthorizationDeniedException e) {
        Map<String, Object> errMap = new HashMap<>();
        errMap.put("message",e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errMap);
    }

    @ExceptionHandler(MemberTaskException.class)
    public ResponseEntity<?> handleArgsException(MemberTaskException e) {
        Map<String, Object> errMap = new HashMap<>();           //비어있는 맵 생성
        errMap.put("error",e.getMessage());

        return ResponseEntity.status(e.getCode()).body(errMap);
    }
}

