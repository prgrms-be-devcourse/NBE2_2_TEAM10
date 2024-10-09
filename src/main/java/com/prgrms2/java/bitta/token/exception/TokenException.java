package com.prgrms2.java.bitta.token.exception;

public enum TokenException {
    EMPTY_HEADER(401, "인증 헤더가 비어있습니다."),
    WRONG_GRANT_TYPE(401, "인증 타입이 일치하지 않습니다."),
    EMPTY_CLAIMS(401, "유효하지 않은 클레임이 존재합니다."),
    ACCESS_AVAILABLE(401, "액세스 토큰이 만료되지 않았습니다."),
    REFRESH_EXPIRED(401, "리프레시 토큰이 만료되었거나 유효하지 않습니다."),
    TOKEN_EXPIRED(401, "토큰이 만료되었습니다."),
    BAD_SIGNATURE(401, "토큰 서명이 올바르지 않습니다."),
    UNSUPPORTED(401, "토큰을 지원하지 않습니다."),
    WRONG_TOKEN(401, "토큰이 올바르지 않습니다.");



    private TokenTaskException tokenTaskException;

    TokenException(int code, String message) {
        tokenTaskException = new TokenTaskException(code, message);
    }

    public TokenTaskException get() {
        return tokenTaskException;
    }
}
