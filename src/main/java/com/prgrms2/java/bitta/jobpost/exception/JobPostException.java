package com.prgrms2.java.bitta.jobpost.exception;

public enum JobPostException {
    NOT_FOUND("NOT_FOUND", 404),
    NOT_REGISTERED("NOT_REGISTERED", 400),
    NOT_MODIFIED("NOT_MODIFIED", 400),
    NOT_REMOVED("NOT_REMOVED", 400),
    NOT_FETCHED("NOT_FETCHED", 400);

    private JobPostTaskException jobPostTaskException;

    JobPostException(String message, int code) {
        jobPostTaskException = new JobPostTaskException(message, code);
    }

    public JobPostTaskException get() {
        return jobPostTaskException;
    }
}
