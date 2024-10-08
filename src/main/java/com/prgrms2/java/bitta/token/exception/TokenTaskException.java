package com.prgrms2.java.bitta.token.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenTaskException extends RuntimeException {
    private int code;
    private String message;
}
