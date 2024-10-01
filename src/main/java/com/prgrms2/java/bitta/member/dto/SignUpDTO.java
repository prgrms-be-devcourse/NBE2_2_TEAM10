package com.prgrms2.java.bitta.member.dto;

import com.prgrms2.java.bitta.member.entity.Member;
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
public class SignUpDTO {

    private String username;
    private String password;
    private String nickname;
    private String address;
    private String profileImg;
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