package com.prgrms2.java.bitta.apply.exception;

public enum ApplyException {
    NOT_FOUND("NOT_FOUND", 404),
    NOT_REGISTERED("NOT_REGISTERED", 400),
    NOT_MODIFIED("NOT_MODIFIED", 400),
    NOT_REMOVED("NOT_REMOVED", 400),
    NOT_FETCHED("NOT_FETCHED", 400);

    private ApplyTaskException applyTaskException;

    ApplyException(String message, int code) {
        applyTaskException = new ApplyTaskException(message, code);
    }

    public ApplyTaskException get() {
        return applyTaskException;
    }
}
