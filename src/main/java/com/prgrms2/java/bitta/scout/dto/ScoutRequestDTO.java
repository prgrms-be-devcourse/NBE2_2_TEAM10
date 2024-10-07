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
    private FeedDTO feed;  // The feed involved in the scout request.
    private Long senderId;  // The ID of the user sending the request.
    private Long receiverId;  // The ID of the user receiving the request.
    private String description;  // Optional description attached to the scout request.
    private LocalDateTime sentAt;  // Timestamp when the request was made.
}