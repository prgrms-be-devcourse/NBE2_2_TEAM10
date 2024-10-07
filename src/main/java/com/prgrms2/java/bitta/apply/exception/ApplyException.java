package com.prgrms2.java.bitta.apply.exception;

public enum ApplyException {
    NOT_FOUND("지원서를 찾을 수 없습니다", 404),
    NOT_REGISTERED("지원서가 등록되지 않았습니다", 400),
    NOT_MODIFIED("지원서가 수정되지 않았습니다", 400),
    NOT_REMOVED("지원서가 삭제되지 않았습니다", 400),
    NOT_FETCHED("지원서 조회에 실패하였습니다", 400);

    private ApplyTaskException applyTaskException;

    ApplyException(String message, int code) {
        applyTaskException = new ApplyTaskException(message, code);
    }

    public ApplyTaskException get() {
        return applyTaskException;
    }
}
