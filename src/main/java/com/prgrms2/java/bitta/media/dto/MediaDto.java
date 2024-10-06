package com.prgrms2.java.bitta.media.dto;

import com.prgrms2.java.bitta.media.entity.MediaCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "미디어 DTO", description = "미디어 파일의 요청 및 응답에 사용하는 DTO입니다.")
public class MediaDto {
    @Schema(title = "미디어 ID (PK)", description = "미디어 파일의 고유 ID 입니다.")
    @Min(value = 1, message = "ID는 0 또는 음수가 될 수 없습니다.")
    private Long id;

    @Schema(title = "파일명", description = "UUID로 이루어진 파일명 입니다.", example = "ef30982a-67e5-4d41-bb34-e05e82798076")
    @NotBlank(message = "파일명은 비우거나, 공백이 될 수 없습니다.")
    @Size(min = 1, max = 36, message = "파일명은 1 ~ 36자 이하여야 합니다.")
    private String filename;

    @Schema(title = "확장자", description = "파일의 확장자 입니다.", example = ".jpg")
    @NotBlank(message = "확장자는 비우거나, 공백이 될 수 없습니다.")
    private String extension;

    @Schema(title = "파일 크기", description = "파일의 크기입니다.", example = "2048")
    @Min(value = 0, message = "파일 크기는 음수가 될 수 없습니다.")
    @Builder.Default
    private Long size = 0L;

    @Schema(title = "파일 타입", description = "파일의 타입입니다.", example = "IMAGE")
    private MediaCategory type;

    @Schema(title = "피드 ID (FK)", description = "피드의 ID입니다.")
    @Min(value = 1, message = "ID는 0 또는 음수가 될 수 없습니다.")
    private Long feedId;

    @Schema(title = "파일 저장일시", description = "파일이 저장된 날짜 및 시간입니다.", example = "2023-09-24T14:45:00")
    private LocalDateTime createdAt;

    @Schema(title = "파일 수정일시", description = "파일이 수정된 날짜 및 시간입니다.", example = "2023-09-24T14:45:00")
    private LocalDateTime updatedAt;
}