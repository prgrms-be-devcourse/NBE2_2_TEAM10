package com.prgrms2.java.bitta.media.exception;

public enum MediaFileException {
    NOT_FOUND(404, "해당 파일은 존재하지 않습니다."),
    INTERNAL_ERROR(500, "파일 처리에 실패했습니다."),
    INVALID_FORMAT(500, "올바르지 않은 파일 포맷입니다.");

    private MediaFileTaskException mediaFileTaskException;

    MediaFileException(int code, String message) {
        mediaFileTaskException = new MediaFileTaskException(code, message);
    }

    public MediaFileTaskException get() {
        return mediaFileTaskException;
    }
}
