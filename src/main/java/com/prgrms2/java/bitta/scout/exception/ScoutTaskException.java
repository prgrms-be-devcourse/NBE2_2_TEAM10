package com.prgrms2.java.bitta.scout.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ScoutTaskException extends RuntimeException {
    private final int code;
    private final String message;
}