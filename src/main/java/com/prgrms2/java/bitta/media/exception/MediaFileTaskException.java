package com.prgrms2.java.bitta.media.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MediaFileTaskException extends RuntimeException {
    private int code;
    private String message;
}
