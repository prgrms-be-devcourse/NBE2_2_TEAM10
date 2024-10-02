package com.prgrms2.java.bitta.member.dto;

import com.prgrms2.java.bitta.apply.dto.ApplyDTO;
import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.member.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Schema(title = "별명", description = "회원의 별명입니다.", example = "Nickname")
    private String nickname;

    @Schema(title = "주소", description = "회원의 주소입니다.", example = "경기도 고양시 일산동구 중앙로 1256")
    private String address;

    @Schema(title = "프로필 이미지 URL", description = "프로필 이미지의 URL 입니다.", example = "IMAGE_URL")
    private String profileImg;

    static public MemberDTO toDTO(Member member) {
        return MemberDTO.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .address(member.getAddress())
                .profileImg(member.getProfileImg()).build();
    }

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .username(username)
                .nickname(nickname)
                .address(address)
                .profileImg(profileImg).build();
    }
}