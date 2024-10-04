package com.prgrms2.java.bitta.member.dto;

import com.prgrms2.java.bitta.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(title = "회원 DTO", description = "회원 요청 및 응답에 사용하는 DTO입니다.")
public class MemberDTO {
    @Schema(title = "회원 ID (PK)", description = "회원의 고유 ID 입니다.", example = "1")
    private Long id;

    @Schema(title = "아이디", description = "로그인에 사용할 회원 아이디입니다.", example = "username")
    private String username;

    @Schema(title = "비밀번호", description = "로그인에 사용할 회원 비밀번호입니다.", example = "password")
    private String password;

    @Schema(title = "별명", description = "회원의 별명입니다.", example = "Nickname")
    private String nickname;

    @Schema(title = "주소", description = "회원의 주소입니다.", example = "경기도 고양시 일산동구 중앙로 1256")
    private String address;

    @Schema(title = "프로필 이미지 URL", description = "프로필 이미지의 URL 입니다.", example = "IMAGE_URL")
    private String profile;

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.nickname = member.getNickname();
        this.address = member.getAddress();
        this.profile = member.getProfile();
    }

    static public MemberDTO toDTO(Member member) {
        return MemberDTO.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .address(member.getAddress())
                .profile(member.getProfile()).build();
    }

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .username(username)
                .nickname(nickname)
                .address(address)
                .profile(profile).build();
    }
}