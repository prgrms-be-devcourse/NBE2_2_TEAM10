package com.prgrms2.java.bitta.member.dto;

import com.prgrms2.java.bitta.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponseDTO {
    private String username;
    private String nickname;
    private String address;
    private String profileUrl;

    public MemberResponseDTO(Member member) {
        this.username = member.getUsername();
        this.nickname = member.getNickname();
        this.address = member.getAddress();
        if(member.getMedia() != null) {
        }
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}