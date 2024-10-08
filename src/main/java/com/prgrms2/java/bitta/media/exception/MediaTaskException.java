package com.prgrms2.java.bitta.media.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MediaTaskException extends RuntimeException {
    private int code;
    private String message;
}
