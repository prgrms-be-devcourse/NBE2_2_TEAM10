package com.prgrms2.java.bitta.member.dto;

import com.prgrms2.java.bitta.apply.dto.ApplyDTO;
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
    private List<ApplyDTO> postApplications;

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
