package com.prgrms2.java.bitta.application.dto;

import com.prgrms2.java.bitta.application.entity.PostApplication;
import com.prgrms2.java.bitta.jobpost.entity.JobPost;
import com.prgrms2.java.bitta.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 받는 생성자 추가
public class PostApplicationDTO {
    private Long applicationId;
    private Long jobPostId;
    private Long memberId;
    private LocalDateTime appliedAt;

    // PostApplication 엔티티로부터 DTO를 생성하는 생성자
    public PostApplicationDTO(PostApplication postApplication) {
        this.applicationId = postApplication.getApplicationId();
        this.jobPostId = postApplication.getJobPost().getJobPostId();
        this.memberId = postApplication.getMember().getMemberId();
        this.appliedAt = postApplication.getAppliedAt();
    }

    // DTO에서 PostApplication 엔티티로 변환하는 메서드
    public PostApplication toEntity(Member member, JobPost jobPost) {
        return PostApplication.builder()
                .applicationId(applicationId) // applicationId가 필요하지 않다면 제거
                .jobPost(jobPost)
                .member(member)
                .appliedAt(appliedAt)
                .build();
    }
}