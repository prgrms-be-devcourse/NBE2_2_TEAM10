package com.prgrms2.java.bitta.member.dto;

import com.prgrms2.java.bitta.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "회원가입 DTO", description = "회원가입 요청에 사용하는 DTO입니다.")
public class SignUpDTO {
    @Schema(title = "아이디", description = "회원가입에 사용할 아이디입니다.", example = "username")
    private String username;

    @Schema(title = "비밀번호", description = "회원가입에 사용할 비밀번호입니다.", example = "password")
    private String password;

    @Schema(title = "별명", description = "회원가입에 사용할 별명입니다.", example = "nickname")
    private String nickname;

    @Schema(title = "주소", description = "회원의 주소입니다.", example = "경기도 고양시 일산동구 중앙로 1256")
    private String address;

    @Schema(title = "프로필 이미지 URL", description = "프로필 이미지의 URL 입니다.", example = "IMAGE_URL")
    private String profileImg;

    @Schema(title = "권한 목록", description = "회원이 가질 권한 목록입니다.", example = "[\"USER\", \"ADMIN\"]")
    private List<String> roles = new ArrayList<>();

    public Member toEntity(String encodedPassword, List<String> roles) {

        return Member.builder()
                .username(username)
                .password(encodedPassword)
                .nickname(nickname)
                .address(address)
                .profileImg(profileImg)
                .roles(roles)
                .build();
    }
}