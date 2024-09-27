package com.prgrms2.java.bitta.member.exception;

public enum MemberException {
    NOT_FOUND("NOT_FOUND", 404),
    DUPLICATE("DUPLICATE", 409),
    INVALID("INVALID", 400),
    BAD_CREDENTIALS("BAD_CREDENTIALS", 401),
    NOT_REGISTER("NOT_REGISTER", 400),
    NOT_MODIFIED("NOT_MODIFIED",400 ),
    REMOVE_FAILED("REMOVE_FAILED", 400);

    private MemberTaskException memberTaskException;

    MemberException(String message, int code) {
        memberTaskException = new MemberTaskException(message, code);
    }

    public MemberTaskException get() {
        return memberTaskException;
    }
}
