package com.prgrms2.java.bitta.jobpost.dto;

import com.prgrms2.java.bitta.jobpost.entity.JobPost;
import com.prgrms2.java.bitta.jobpost.entity.Location;
import com.prgrms2.java.bitta.jobpost.entity.PayStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class JobPostDTO {
    private Long jobPostId;
    private Long memberId;

    private String title;
    private String description;
    private Location location;
    private PayStatus payStatus;

    private LocalDateTime updateAt;
    private LocalDate startDate;
    private LocalDate endDate;

    private boolean isClosed;

    public JobPostDTO(JobPost jobPost) {
        this.jobPostId = jobPostId;
        this.memberId = jobPost.getMember().getMemberId();
        this.title = title;
        this.description = description;
        this.location = location;
        this.payStatus = payStatus;
        this.updateAt = updateAt;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isClosed = isClosed;
    }

    public JobPost toEntity() {
        JobPost jobPost = JobPost.builder().title(title)
                .description(description)
                .location(location)
                .payStatus(payStatus)
                .startDate(startDate)
                .endDate(endDate).build();
        return jobPost;
    }
}
