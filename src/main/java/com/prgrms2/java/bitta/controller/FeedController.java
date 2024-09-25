package com.prgrms2.java.bitta.controller;

import com.prgrms2.java.bitta.dto.FeedDto;
import com.prgrms2.java.bitta.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

    @GetMapping
    public ResponseEntity<?> getFeed() {
        List<FeedDto> feedDtoList = feedService.readAll();

        if (feedDtoList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(feedDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFeedById(@PathVariable("id") Long feedId) {
        Optional<FeedDto> feedDto = feedService.read(feedId);

        if (feedDto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(feedDto.get());
    }

    @PostMapping
    public ResponseEntity<?> createFeed(@RequestBody FeedDto feedDto) {
        if (feedDto == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(feedService.insert(feedDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifyFeed(@PathVariable("id") Long feedId, @RequestBody FeedDto feedDto) {
        if (feedDto == null) {
            return ResponseEntity.badRequest().build();
        }

        feedDto.setFeedId(feedId);

        return ResponseEntity.ok(feedService.update(feedDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeed(@PathVariable("id") Long feedId) {
        return feedService.delete(feedId) ?
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
