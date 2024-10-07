package com.prgrms2.java.bitta.member.exception;

public enum MemberException {

    NOT_FOUND(404, "회원이 존재하지 않습니다."),
    DUPLICATE(409, "중복된 회원입니다."),
    INVALID(400, "잘못된 요청입니다."),
    BAD_CREDENTIALS(401, "잘못된 자격 증명입니다."),
    NOT_REGISTER(400, "회원 등록이 완료되지 않았습니다."),
    NOT_MODIFIED(304, "변경된 내용이 없습니다."),
    REMOVE_FAILED(400, "회원 삭제에 실패했습니다."),
    INTERNAL_ERROR(500, "서버 내부에 오류가 발생했습니다.");

    private final MemberTaskException memberTaskException;

    MemberException(int code, String message) {
        this.memberTaskException = new MemberTaskException(message, code);
    }

    public MemberTaskException get() {
        return memberTaskException;
    }
}
