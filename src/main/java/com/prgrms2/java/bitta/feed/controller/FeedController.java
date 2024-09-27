package com.prgrms2.java.bitta.feed.controller;

import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.feed.service.FeedService;
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
        List<FeedDTO> feedDTOList = feedService.readAll();

        if (feedDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(feedDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFeedById(@PathVariable("id") Long feedId) {
        Optional<FeedDTO> feedDto = feedService.read(feedId);

        if (feedDto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(feedDto.get());
    }

    @PostMapping
    public ResponseEntity<?> createFeed(@RequestBody FeedDTO feedDto) {
        if (feedDto == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(feedService.insert(feedDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifyFeed(@PathVariable("id") Long feedId, @RequestBody FeedDTO feedDto) {
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
