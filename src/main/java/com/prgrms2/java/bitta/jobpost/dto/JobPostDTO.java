package com.prgrms2.java.bitta.jobpost.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms2.java.bitta.jobpost.entity.JobPost;
import com.prgrms2.java.bitta.jobpost.entity.Location;
import com.prgrms2.java.bitta.jobpost.entity.PayStatus;
import com.prgrms2.java.bitta.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@Schema(title = "일거리 DTO", description = "일거리의 요청 및 응답에 사용하는 DTO입니다.")
public class JobPostDTO {
    @Schema(title = "일거리 ID (PK)", description = "일거리의 고유 ID 입니다.", example = "1")
    private Long id;

    @Schema(title = "회원 ID (FK)", description = "회원의 고유 ID 입니다.", example = "1")
    private Long userId;

    @Schema(title = "일거리 제목", description = "일거리 제목입니다.", example = "Job Title")
    private String title;

    @Schema(title = "일거리 내용", description = "일거리 내용입니다.", example = "Job Content")
    private String description;

    @Schema(title = "출근지", description = "출근 지역입니다.", example = "SEOUL")
    private Location location;

    @Schema(title = "지불 유형", description = "지불 유형입니다.", example = "FREE")
    private PayStatus payStatus;

    @Schema(title = "일거리 수정일시", description = "일거리가 수정된 날짜 및 시간입니다.", example = "2023-09-24T14:45:00")
    private LocalDateTime updateAt;

    @Schema(title = "시작일", description = "일이 시작하는 날짜입니다.", example = "2023-09-24")
    private LocalDate startDate;

    @Schema(title = "종료일", description = "일이 종료되는 날짜입니다.", example = "2023-09-24")
    private LocalDate endDate;

    @Schema(title = "종료여부", description = "일이 종료되었는지 여부입니다.", example = "true")
    @JsonProperty("isClosed")
    private boolean isClosed;
}
