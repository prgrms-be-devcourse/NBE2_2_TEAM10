package com.prgrms2.java.bitta.scout.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoutDTO {
    private Long id;
    private Long feedId;
    private Long senderId;
    private Long receiverId;
    private String description;
    private LocalDateTime sentAt;
}