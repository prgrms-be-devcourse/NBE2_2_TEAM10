package com.prgrms2.java.bitta.jobpost.dto;

import com.prgrms2.java.bitta.apply.entity.Apply;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ApplyDTO {
    private Long applicationId;
    private Long jobPostId;
    private Long userId;
    private LocalDateTime appliedAt;

    public ApplyDTO(Apply apply) {
        this.applicationId = apply.getApplicationId();
        this.jobPostId = apply.getJobPost().getJobPostId();
        this.userId = apply.getMember().getMemberId();
        this.appliedAt = apply.getAppliedAt();
    }
}

