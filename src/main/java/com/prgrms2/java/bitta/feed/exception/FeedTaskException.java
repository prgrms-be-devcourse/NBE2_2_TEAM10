package com.prgrms2.java.bitta.feed.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FeedTaskException extends RuntimeException {
    private int code;
    private String message;
}
