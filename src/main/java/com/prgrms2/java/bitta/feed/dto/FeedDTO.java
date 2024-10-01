package com.prgrms2.java.bitta.feed.dto;

import com.prgrms2.java.bitta.feed.entity.Feed;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import com.prgrms2.java.bitta.photo.entity.Photo;
import com.prgrms2.java.bitta.video.entity.Video;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedDTO {
    @Min(value = 1, message = "ID는 음수가 될 수 없습니다.")
    private Long id;

    @NotBlank(message = "제목은 비워둘 수 없습니다.")
    @Size(min = 1, max = 50, message = "제목은 1 ~ 50자 이하여야 합니다.")
    private String title;

    @NotNull
    @Builder.Default
    private String content = "";

    @Min(value = 1, message = "ID는 음수가 될 수 없습니다.")
    @NotBlank(message = "회원 ID는 누락될 수 없습니다.")
    private Long memberId;

    @PastOrPresent(message = "생성일자는 현재 시점 혹은 이전이어야 합니다.")
    private LocalDateTime createdAt;

    private List<String> photoUrls;

    private List<String> videoUrls;
}
