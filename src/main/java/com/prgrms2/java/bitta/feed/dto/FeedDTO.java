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

    @NotBlank(message = "이메일은 비워둘 수 없습니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;

    @PastOrPresent(message = "생성일자는 현재 시점 혹은 이전이어야 합니다.")
    private LocalDateTime createdAt;


    //photo 와 video DTO
    private List<String> photoUrls;
    private List<String> videoUrls;

    public FeedDTO(Feed feed) {
        this.id = feed.getId();
        this.title = feed.getTitle();
        this.content = feed.getContent();
        this.email = feed.getMember().getEmail();
        this.createdAt = feed.getCreatedAt();


        this.photoUrls = feed.getPhotos().stream()
                .map(Photo::getPhotoUrl)
                .toList();
        this.videoUrls = feed.getVideos().stream()
                .map(Video::getVideoUrl)
                .toList();
    }

}
