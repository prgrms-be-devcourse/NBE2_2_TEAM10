package com.prgrms2.java.bitta.member.dto;

import com.prgrms2.java.bitta.application.dto.PostApplicationDTO;
import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    private Long memberId;
    private String memberName;
    private String password;
    private String email;
    private String location;
    private Role role;
    private String profilePicture;
    private LocalDateTime createdAt;
    private List<FeedDTO> feeds;
    private List<PostApplicationDTO> postApplications;

    public MemberDTO(Member member) {
        this.memberId = member.getMemberId();
        this.memberName = member.getMemberName();
        this.password = member.getPassword();
        this.email = member.getEmail();
        this.location = member.getLocation();
        this.role = member.getRole();
        this.profilePicture = member.getProfilePicture();
        this.createdAt = member.getCreatedAt();

        // Feed와 PostApplication 정보도 가져옴
        this.feeds = member.getFeeds().stream()
                .map(FeedDTO::new)
                .collect(Collectors.toList());
        this.postApplications = member.getPostApplications().stream()
                .map(PostApplicationDTO::new)
                .collect(Collectors.toList());

    }

    public Member toEntity(){
        return Member.builder()
                .memberId(memberId)
                .memberName(memberName)
                .password(password)
                .email(email)
                .location(location)
                .role(role)
                .profilePicture(profilePicture)
                .createdAt(createdAt)
                .build();
    }

    // JWT
    public Map<String, Object> getPayload() {
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("memberId", memberId);
        payloadMap.put("memberName", memberName);
        payloadMap.put("email", email);
        payloadMap.put("role", role);
        return payloadMap;
    }
}
