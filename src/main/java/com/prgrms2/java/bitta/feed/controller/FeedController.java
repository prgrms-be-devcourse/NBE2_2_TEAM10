package com.prgrms2.java.bitta.feed.controller;

import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.feed.service.FeedService;
import com.prgrms2.java.bitta.member.service.MemberServiceImpl;
import com.prgrms2.java.bitta.photo.service.PhotoService;
import com.prgrms2.java.bitta.video.service.VideoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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
    public ResponseEntity<?> createFeed(@RequestBody @Valid FeedDTO feedDto,
                                        @RequestParam(value = "photos", required = false) List<MultipartFile> photos,
                                        @RequestParam(value = "videos", required = false) List<MultipartFile> videos) {
        feedService.insert(feedDto, photos, videos);

        return ResponseEntity.ok().body(Map.of("message", "피드가 등록되었습니다."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifyFeed(@PathVariable("id") @Min(1) Long id, @RequestBody @Valid FeedDTO feedDTO,
                                        @RequestParam(value = "photos", required = false) List<MultipartFile> photos,
                                        @RequestParam(value = "videos", required = false) List<MultipartFile> videos) {
        feedDTO.setId(id);

        feedService.update(feedDTO, photos, videos);

        return ResponseEntity.ok().body(Map.of("message", "피드가 수정되었습니다."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeed(@PathVariable("id") @Min(1) Long id) {
        feedService.delete(id);

        return ResponseEntity.ok().body(Map.of("message", "피드가 삭제되었습니다."));
    }

}

