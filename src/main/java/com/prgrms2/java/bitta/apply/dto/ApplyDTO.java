package com.prgrms2.java.bitta.apply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 받는 생성자 추가
public class ApplyDTO {
    private Long id;
    private Long jobPostId;
    private Long memberId;
    private LocalDateTime appliedAt;
}