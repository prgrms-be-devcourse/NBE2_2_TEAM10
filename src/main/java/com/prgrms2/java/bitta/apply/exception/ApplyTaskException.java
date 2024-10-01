package com.prgrms2.java.bitta.apply.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplyTaskException extends RuntimeException {
    private String message;
    private int code;
}
