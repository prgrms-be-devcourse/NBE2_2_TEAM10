package com.prgrms2.java.bitta.jobpost.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms2.java.bitta.jobpost.entity.JobPost;
import com.prgrms2.java.bitta.jobpost.entity.Location;
import com.prgrms2.java.bitta.jobpost.entity.PayStatus;
import com.prgrms2.java.bitta.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class JobPostDTO {
    private Long id;
    private Long userId;

    private String title;
    private String description;
    private Location location;
    private PayStatus payStatus;

    private LocalDateTime updateAt;
    private LocalDate startDate;
    private LocalDate endDate;

    @JsonProperty("isClosed")
    private boolean isClosed;

    public JobPostDTO(JobPost jobPost) {
        this.id = jobPost.getId();
        this.userId = jobPost.getMember().getMemberId();
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
        return JobPost.builder().id(id)
                .title(title)
                .description(description)
                .location(location)
                .payStatus(payStatus)
                .updatedAt(updateAt)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
