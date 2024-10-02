package com.prgrms2.java.bitta.global.constants;

public class ApiResponses {
    // 피드 관련 성공 응답 바디 
    public static final String FEED_SUCCESS_READ_ALL = "{\"message\": \"피드를 성공적으로 조회했습니다.\", \"result\": [{ \"id\": 1, \"title\": \"Sample Title1\", \"content\": \"Sample Content1\", \"memberId\": 1, \"createdAt\": \"2023-09-24T00:00:00\", \"photoUrls\": [\"IMAGE_URL_1\", \"IMAGE_URL_2\"], \"videoUrl\": [\"VIDEO_URL_1\", \"VIDEO_URL_2\"]}, { \"id\": 2, \"title\": \"Sample Title2\", \"content\": \"Sample Content2\", \"memberId\": 1, \"createdAt\": \"2023-09-24T00:00:00\", \"photoUrls\": [\"IMAGE_URL_3\", \"IMAGE_URL_4\"], \"videoUrl\": [\"VIDEO_URL_3\", \"VIDEO_URL_4\"]}]}";
    public static final String FEED_SUCCESS_READ = "{\"message\": \"피드를 성공적으로 조회했습니다.\", \"result\": {\"id\": 1, \"title\": \"Sample Title\", \"content\": \"Sample Content\", \"memberId\": 1, \"createdAt\": \"2023-09-24T14:45:00\", \"photoUrls\": [\"IMAGE_URL_1\", \"IMAGE_URL_2\"], \"videoUrl\": [\"VIDEO_URL_1\", \"VIDEO_URL_2\"]}}";
    public static final String FEED_SUCCESS_CREATE = "{\"message\": \"피드가 등록되었습니다.\"}";
    public static final String FEED_SUCCESS_MODIFY = "{\"message\": \"피드가 수정되었습니다.\"}";
    public static final String FEED_SUCCESS_DELETE = "{\"message\": \"피드가 삭제되었습니다.\"}";

    // 피드 관련 실패 응답 바디
    public static final String FEED_FAILURE_WRONG_PATH_VARIABLE = "{\"error\": \"잘못된 요청입니다.\", \"reason\": \"ID는 음수가 될 수 없습니다.\"}";
    public static final String FEED_FAILURE_WRONG_REQUEST_BODY = "{\"error\": \"잘못된 요청입니다.\", \"reason\": \"제목은 비워둘 수 없습니다.\"}";
    public static final String FEED_FAILURE_BAD_REQUEST = "{\"error\": \"잘못된 요청입니다.\"}";
    public static final String FEED_FAILURE_NOT_FOUND = "{\"error\": \"피드가 존재하지 않습니다.\"}";
    public static final String FEED_FAILURE_INTERNAL_ERROR = "{\"error\": \"서버 내부에 오류가 발생했습니다.\"}";
    public static final String FEED_FAILURE_DELETE = "{\"error\": \"삭제할 피드가 존재하지 않습니다.\"}";

    // 지원서 관련 성공 응답 바디
    public static final String APPLY_SUCCESS_READ_ALL = "[{\"id\": 1, \"jobPostId\": 1, \"memberId\": 1, \"appliedAt\": \"2023-09-24T00:00:00\"},{\"id\": 2, \"jobPostId\": 2, \"memberId\": 2, \"appliedAt\": \"2023-09-25T00:00:00\"}]";
    public static final String APPLY_SUCCESS_READ = "{\"id\": 1, \"jobPostId\": 1, \"memberId\": 1, \"appliedAt\": \"2023-09-24T00:00:00\"}";
    public static final String APPLY_SUCCESS_REGISTER = "{\"id\": 1, \"jobPostId\": 1, \"memberId\": 1, \"appliedAt\": \"2023-09-24T00:00:00\"}";
    public static final String APPLY_SUCCESS_DELETE = "{\"message\": \"delete complete\"}";

    // 지원서 관련 실패 응답 바디
    public static final String APPLY_FAILURE_NOT_FOUND = "{\"message\": \"NOT_FOUND\", \"code\": 404}";
    public static final String APPLY_FAILURE_NOT_REGISTERED = "{\"message\": \"NOT_REGISTERED\", \"code\": 400}";
    public static final String APPLY_FAILURE_NOT_REMOVED = "{\"message\": \"NOT_REMOVED\", \"code\": 400}";

    // 일거리 관련 성공 응답 바디
    public static final String JOB_POST_SUCCESS_READ_ALL = "[{\"id\": 1, \"userId\": 1, \"title\": \"Job Title 1\", \"description\": \"Job Description 1\", \"location\": \"SEOUL\", \"payStatus\": \"FREE\", \"updatedAt\": \"2024-10-01T19:50:18\", \"startDate\": \"2023-09-24\", \"endDate\": \"2023-09-24\", \"isClosed\": true},{\"id\": 2, \"userId\": 2, \"title\": \"Job Title 2\", \"description\": \"Job Description 2\", \"location\": \"SEOUL\", \"payStatus\": \"FREE\", \"updatedAt\": \"2024-10-01T19:50:18\", \"startDate\": \"2023-09-24\", \"endDate\": \"2023-09-24\", \"isClosed\": true}]";
    public static final String JOB_POST_SUCCESS_READ = "{\"id\": 1, \"userId\": 1, \"title\": \"Job Title 1\", \"description\": \"Job Description 1\", \"location\": \"SEOUL\", \"payStatus\": \"FREE\", \"updatedAt\": \"2024-10-01T19:50:18\", \"startDate\": \"2023-09-24\", \"endDate\": \"2023-09-24\", \"isClosed\": true}";
    public static final String JOB_POST_SUCCESS_REGISTER = "{\"id\": 1, \"userId\": 1, \"title\": \"Job Title 1\", \"description\": \"Job Description 1\", \"location\": \"SEOUL\", \"payStatus\": \"FREE\", \"updatedAt\": \"2024-10-01T19:50:18\", \"startDate\": \"2023-09-24\", \"endDate\": \"2023-09-24\", \"isClosed\": true}";
    public static final String JOB_POST_SUCCESS_MODIFY = "{\"id\": 1, \"userId\": 1, \"title\": \"Job Title 1\", \"description\": \"Job Description 1\", \"location\": \"SEOUL\", \"payStatus\": \"FREE\", \"updatedAt\": \"2024-10-01T19:50:18\", \"startDate\": \"2023-09-24\", \"endDate\": \"2023-09-24\", \"isClosed\": true}";
    public static final String JOB_POST_SUCCESS_DELETE = "{\"message\": \"success\"}";

    // 일거리 관련 실패 응답 바디
    public static final String JOB_POST_FAILURE_NOT_FOUND = "{\"message\": \"NOT_FOUND\", \"code\": 404}";
    public static final String JOB_POST_FAILURE_NOT_REGISTERED = "{\"message\": \"NOT_REGISTERED\", \"code\": 400}";
    public static final String JOB_POST_FAILURE_NOT_MODIFIED = "{\"message\": \"NOT_MODIFIED\", \"code\": 400}";
    public static final String JOB_POST_FAILURE_NOT_REMOVED = "{\"message\": \"NOT_REMOVED\", \"code\": 400}";
    
    // 회원 관련 성공 응답 바디
    public static final String MEMBER_SUCCESS_SIGN_UP = "{\"id\": 1, \"username\": \"username\", \"nickname\": \"nickname\", \"address\": \"경기도 고양시 일산동구 중앙로 1256\", \"profileImg\": \"PROFILE_URL\"}";
    public static final String MEMBER_SUCCESS_UPDATE_PROFILE_IMAGE = "{\"message\": \"프로필이미지 수정이 완료되었습니다.\"}";
    public static final String MEMBER_SUCCESS_DELETE_PROFILE_IMAGE = "{\"message\": \"프로필이미지가 삭제되었습니다.\"}";
}
