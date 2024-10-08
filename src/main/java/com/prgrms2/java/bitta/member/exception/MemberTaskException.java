package com.prgrms2.java.bitta.member.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberTaskException extends RuntimeException {
    private int code;
    private String message;
}
