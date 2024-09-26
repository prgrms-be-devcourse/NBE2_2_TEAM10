package com.prgrms2.java.bitta.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

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
        this.jobPostId = jobPostId;
        this.userId = userId;
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
