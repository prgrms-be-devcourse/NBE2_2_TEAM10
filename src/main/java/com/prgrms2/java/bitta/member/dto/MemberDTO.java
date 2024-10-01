package com.prgrms2.java.bitta.member.dto;

import com.prgrms2.java.bitta.apply.dto.ApplyDTO;
import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.member.entity.Role;
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
public class MemberDTO {

    private Long id;
    private String username;
    private String nickname;
    private String address;
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