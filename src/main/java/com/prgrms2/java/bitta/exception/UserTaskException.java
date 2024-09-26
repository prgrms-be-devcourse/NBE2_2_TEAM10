package com.prgrms2.java.bitta.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserTaskException extends RuntimeException {
    private String message;
    private int code;
}
