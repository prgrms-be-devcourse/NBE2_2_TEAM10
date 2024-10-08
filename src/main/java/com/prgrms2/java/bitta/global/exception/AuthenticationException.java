package com.prgrms2.java.bitta.global.exception;

public enum AuthenticationException {
    CANNOT_ACCESS(403, "해당 API에 대한 액세스 권한이 없습니다.");

    private AuthenticationTaskException authenticationTaskException;

    AuthenticationException(int code, String message) {
        authenticationTaskException = new AuthenticationTaskException(code, message);
    }

    public AuthenticationTaskException get() {
        return authenticationTaskException;
    }
}
