package com.prgrms2.java.bitta.feed.controller;

import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.feed.service.FeedService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
@Validated
public class FeedController {
    private final FeedService feedService;

    @GetMapping
    public ResponseEntity<?> getFeed() {
        return ResponseEntity.ok(
                Map.of("message", "피드를 성공적으로 조회했습니다.", "result", feedService.readAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFeedById(@PathVariable("id") @Min(1) Long id) {
        return ResponseEntity.ok(
                Map.of("message", "피드를 성공적으로 조회했습니다.", "result", feedService.read(id))
        );
    }

    @PostMapping
    public ResponseEntity<?> createFeed(@RequestBody @Valid FeedDTO feedDto) {
        feedService.insert(feedDto);

        return ResponseEntity.ok().body(Map.of("message", "피드가 등록되었습니다."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifyFeed(@PathVariable("id") @Min(1) Long id, @RequestBody @Valid FeedDTO feedDTO) {
        feedDTO.setId(id);

        feedService.update(feedDTO);

        return ResponseEntity.ok().body(Map.of("message", "피드가 수정되었습니다."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeed(@PathVariable("id") @Min(1) Long id) {
        feedService.delete(id);

        return ResponseEntity.ok().body(Map.of("message", "피드가 삭제되었습니다."));
    }
}
