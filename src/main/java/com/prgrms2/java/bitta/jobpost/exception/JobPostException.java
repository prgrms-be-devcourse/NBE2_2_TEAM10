package com.prgrms2.java.bitta.jobpost.exception;

public enum JobPostException {
    BAD_REQUEST("잘못된 접근입니다", 400),
    NOT_FOUND("게시글을 찾을 수 없습니다", 404),
    NOT_REGISTERED("게시글을 등록할 수 없습니다", 400),
    NOT_MODIFIED("게시글을 수정할 수 없습니다", 400),
    NOT_REMOVED("게시글을 삭제할 수 없습니다", 400),
    NOT_FETCHED("게시글을 조회할 수 없습니다", 400);

    private JobPostTaskException jobPostTaskException;

    JobPostException(String message, int code) {
        jobPostTaskException = new JobPostTaskException(message, code);
    }

    public JobPostTaskException get() {
        return jobPostTaskException;
    }
}
