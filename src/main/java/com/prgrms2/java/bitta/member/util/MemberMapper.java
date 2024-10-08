package com.prgrms2.java.bitta.member.util;

import com.prgrms2.java.bitta.member.dto.MemberRequestDto;
import com.prgrms2.java.bitta.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    public Member dtoToEntity(MemberRequestDto.Register dto) {
        return Member.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .address(dto.getAddress())
                .role(dto.getRole())
                .build();
    }

    public Member dtoToEntity(MemberRequestDto.ChangePassword dto) {
        return Member.builder()
                .id(dto.getId())
                .password(dto.getAfterPassword())
                .build();
    }

    public Member dtoToEntity(MemberRequestDto.Modify dto) {
        return Member.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .nickname(dto.getNickname())
                .address(dto.getAddress())
                .build();
    }
}
