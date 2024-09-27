package com.prgrms2.java.bitta.dto;

import com.prgrms2.java.bitta.entity.Apply;
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
        this.userId = apply.getUser().getUserId();
        this.appliedAt = apply.getAppliedAt();
    }
}

