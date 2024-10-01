package com.prgrms2.java.bitta.feed.controller;

import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.feed.service.FeedService;
import com.prgrms2.java.bitta.member.service.MemberService;
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
    private final PhotoService photoService;
    private final VideoService videoService;
    private final MemberService memberService;



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


    @PostMapping("/{id}/photos")
    public ResponseEntity<?> addPhotosToFeed(@PathVariable("id") Long feedId, @RequestParam("photos") List<MultipartFile> photos) {
        try {
            feedService.addPhotosToFeed(feedId, photos);
            return ResponseEntity.ok().body(Map.of("message", "사진이 업로드되었습니다."));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("사진 업로드 실패");
        }

    }

    @PostMapping("/{id}/videos")
    public ResponseEntity<?> addVideosToFeed(@PathVariable("id") Long feedId, @RequestParam("videos") List<MultipartFile> videos) {
        try {
            feedService.addVideosToFeed(feedId, videos);
            return ResponseEntity.ok().body(Map.of("message", "비디오가 업로드되었습니다."));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("비디오 업로드 실패");
        }

    }
}

