package com.prgrms2.java.bitta.jobpost.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JobPostTaskException extends RuntimeException {
    private String message;
    private int code;
}
