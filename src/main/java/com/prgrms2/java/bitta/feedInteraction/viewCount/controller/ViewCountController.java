package com.prgrms2.java.bitta.feedInteraction.viewCount.controller;

import com.prgrms2.java.bitta.feedInteraction.viewCount.service.ViewCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feed/view")
@RequiredArgsConstructor
public class ViewCountController {

    private final ViewCountService viewCountService;

    // 조회수 증가 API
    @PostMapping("/{feedId}")
    public ResponseEntity<String> addView(@PathVariable Long feedId) {
        viewCountService.addView(feedId);
        return ResponseEntity.ok("View count increased");
    }

    // 조회수 확인 API
    @GetMapping("/{feedId}/count")
    public ResponseEntity<Long> getViewCount(@PathVariable Long feedId) {
        Long viewCount = viewCountService.getViewCount(feedId);
        return ResponseEntity.ok(viewCount);
    }
}