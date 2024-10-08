package com.prgrms2.java.bitta.media.exception;

public enum MediaException {
    BAD_REQUEST(400, "올바르지 않은 접근 경로입니다."),
    NOT_FOUND(404, "해당 파일은 존재하지 않습니다."),
    INTERNAL_ERROR(500, "파일 처리에 실패했습니다."),
    INVALID_FORMAT(500, "올바르지 않은 파일 포맷입니다.");

    private MediaTaskException mediaTaskException;

    MediaException(int code, String message) {
        mediaTaskException = new MediaTaskException(code, message);
    }

    public MediaTaskException get() {
        return mediaTaskException;
    }
}
