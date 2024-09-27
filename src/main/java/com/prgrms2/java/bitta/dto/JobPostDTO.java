package com.prgrms2.java.bitta.dto;

import com.prgrms2.java.bitta.entity.JobPost;
import com.prgrms2.java.bitta.entity.Location;
import com.prgrms2.java.bitta.entity.PayStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class JobPostDTO {
    private Long jobPostId;
    private Long userId;

    private String title;
    private String description;
    private Location location;
    private PayStatus payStatus;

    private LocalDateTime updateAt;
    private LocalDate startDate;
    private LocalDate endDate;

    private boolean isClosed;

    public JobPostDTO(JobPost jobPost) {
        this.jobPostId = jobPost.getJobPostId();
        this.userId = jobPost.getUser().getUserId();
        this.title = jobPost.getTitle();
        this.description = jobPost.getDescription();
        this.location = jobPost.getLocation();
        this.payStatus = jobPost.getPayStatus();
        this.updateAt = jobPost.getUpdatedAt();
        this.startDate = jobPost.getStartDate();
        this.endDate = jobPost.getEndDate();
        this.isClosed = jobPost.isClosed();
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
