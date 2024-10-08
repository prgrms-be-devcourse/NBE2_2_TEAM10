package com.prgrms2.java.bitta.global.advice;

import com.prgrms2.java.bitta.global.exception.AuthenticationTaskException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleArgsException(MethodArgumentNotValidException e) {
        List<String> reasons = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "잘못된 요청입니다.", "reason", reasons));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleArgsException(ConstraintViolationException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", "잘못된 요청입니다.", "reason", "ID는 음수가 될 수 없습니다."));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleFileSizeLimitExceeded(MaxUploadSizeExceededException e) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE) // Status 413
                .body(Map.of("error", "Payload Too Large", "reason", "파일의 크기가 허용된 한도를 초과했습니다."));
    }

    @ExceptionHandler(AuthenticationTaskException.class)
    public ResponseEntity<?> handleArgsException(AuthenticationTaskException e) {
        return ResponseEntity.status(e.getCode())
                .body(Map.of("error", e.getMessage()));
    }
}
