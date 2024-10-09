package com.prgrms2.java.bitta.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MemberResponseDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(title = "회원정보 DTO", description = "회원정보 요청에 사용하는 DTO입니다.")
    public static class Information {
        @Schema(title = "회원 ID (PK)", description = "조회한 회원의 기본키입니다.", example = "1")
        private Long id;

        @Schema(title = "아이디", description = "조회한 회원의 아이디입니다.", example = "username")
        private String username;

        @Schema(title = "닉네임", description = "조회한 회원의 별명입니다.", example = "nickname")
        private String nickname;

        @Schema(title = "주소", description = "조회한 회원의 주소입니다.", example = "경기도 고양시 일산동구 중앙로 1256")
        private String address;

        @Schema(title = "프로필 이미지 URL", description = "프로필 이미지의 URL 입니다.", example = "IMAGE_URL")
        private String profileUrl;
    }
}
