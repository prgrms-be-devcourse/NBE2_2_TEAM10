package com.prgrms2.java.bitta.gobal.exception;

public enum UserException {
    NOT_FOUND("NOT_FOUND", 404),
    DUPLICATE("DUPLICATE", 409),
    INVALID("INVALID", 400),
    BAD_CREDENTIALS("BAD_CREDENTIALS", 401);

    private UserTaskException userTaskException;

    UserException(String message, int code) {
        userTaskException = new UserTaskException(message, code);
    }

    public UserTaskException get() {
        return userTaskException;
    }
}
