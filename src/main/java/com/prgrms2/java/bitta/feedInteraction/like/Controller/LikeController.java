package com.example.feedinteraction.controller;


import com.prgrms2.java.bitta.feedInteraction.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feed/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{feedId}")
    public ResponseEntity<String> toggleLike(@PathVariable Long feedId, @RequestParam Long memberId) {
        boolean isLiked = likeService.toggleLike(feedId, memberId);
        return ResponseEntity.ok(isLiked ? "Liked" : "Unliked");
    }

    @GetMapping("/{feedId}/count")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long feedId) {
        long likeCount = likeService.getLikeCount(feedId);
        return ResponseEntity.ok(likeCount);
    }
}