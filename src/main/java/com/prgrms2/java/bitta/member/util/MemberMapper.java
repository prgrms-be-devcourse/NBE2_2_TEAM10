package com.prgrms2.java.bitta.member.util;

import com.prgrms2.java.bitta.member.dto.MemberRequestDto;
import com.prgrms2.java.bitta.member.dto.MemberResponseDto;
import com.prgrms2.java.bitta.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    public Member dtoToEntity(MemberRequestDto.Join JoinDTO) {
        return Member.builder()
                .username(JoinDTO.getUsername())
                .password(JoinDTO.getPassword())
                .nickname(JoinDTO.getNickname())
                .address(JoinDTO.getAddress())
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

    public MemberResponseDto.Information entityToDto(Member member) {
        return MemberResponseDto.Information.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .address(member.getAddress())
                .build();
    }
}
