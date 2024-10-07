package com.prgrms2.java.bitta.scout.dto;

import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoutRequestDTO {
    private Long id;
    private FeedDTO feed;
    private Long senderId;
    private Long receiverId;
    private String description;
    private LocalDateTime sentAt;
}