package com.prgrms2.java.bitta.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FeedDto {
    private Long feedId;

    private String title;

    private String content;

    private Long userId;

    private LocalDateTime createdAt;
}
