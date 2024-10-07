package com.prgrms2.java.bitta.feed.dto;

import com.prgrms2.java.bitta.media.dto.MediaDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "피드 DTO", description = "피드의 요청 및 응답에 사용하는 DTO입니다.")
public class FeedDTO {
    @Schema(title = "피드 ID (PK)", description = "피드의 고유 ID 입니다.", example = "1", minimum = "1")
    @Min(value = 1, message = "ID는 0 또는 음수가 될 수 없습니다.")
    private Long id;

    @Schema(title = "피드 제목", description = "피드 제목입니다.", example = "Feed Title", minimum = "1", maximum = "50")
    @NotBlank(message = "제목은 비우거나, 공백이 될 수 없습니다.")
    @Size(min = 1, max = 50, message = "제목은 1 ~ 50자 이하여야 합니다.")
    private String title;

    @Schema(title = "피드 내용", description = "피드 내용입니다.", example = "Feed Content")
    @NotNull
    @Builder.Default
    private String content = "";

    @Schema(title = "회원 ID (FK)", description = "회원의 고유 ID 입니다.", example = "1", minimum = "1")
    @Min(value = 1, message = "ID는 0 또는 음수가 될 수 없습니다.")
    @NotNull(message = "회원 ID는 누락될 수 없습니다.")
    private Long memberId;

    @Schema(title = "피드 생성일시", description = "피드가 생성된 날짜 및 시간입니다.", example = "2023-09-24T14:45:00")
    private LocalDateTime createdAt;

    @Schema(title = "미디어 파일 목록", description = "피드에 포함된 사진 및 영상 목록입니다.")
    private List<MediaDto> medias;
}