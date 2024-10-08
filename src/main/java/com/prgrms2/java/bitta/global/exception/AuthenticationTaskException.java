package com.prgrms2.java.bitta.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationTaskException extends RuntimeException {
    private int code;
    private String message;
}
