package com.prgrms2.java.bitta.apply.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "지원서 DTO", description = "지원서 요청 및 응답에 사용하는 DTO입니다.")
public class ApplyDTO {
    @Schema(title = "지원서 ID (PK)", description = "지원서의 고유 ID 입니다.", example = "1", minimum = "1")
    @Min(value = 1, message = "ID는 음수가 될 수 없습니다")
    private Long id;

    @Schema(title = "일거리 ID (FK)", description = "일거리의 고유 ID 입니다.", example = "1", minimum = "1")
    @Min(value = 1, message = "ID는 음수가 될 수 없습니다")
    @NotNull(message = "게시글의 ID가 필요합니다")
    private Long jobPostId;
    
    @Schema(title = "회원 ID (FK)", description = "회원의 고유 ID 입니다.", example = "1", minimum = "1")
    @Min(value = 1, message = "ID는 음수가 될 수 없습니다")
    @NotNull(message = "회원의 ID가 필요합니다")
    private Long memberId;

    @Schema(title = "지원서 생성일시", description = "지원서가 생성된 날짜 및 시간입니다.", example = "2023-09-24T14:45:00")
    private LocalDateTime appliedAt;

}