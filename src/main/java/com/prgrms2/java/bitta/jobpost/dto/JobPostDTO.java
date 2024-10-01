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
}
