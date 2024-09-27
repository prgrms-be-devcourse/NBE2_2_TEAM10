package com.prgrms2.java.bitta.feed.controller;

import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.feed.service.FeedService;
import com.prgrms2.java.bitta.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;
    private final PhotoService photoService;

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
    public ResponseEntity<?> createFeed(@RequestBody FeedDTO feedDto,
                                        @RequestParam(value = "photo", required = false) List<MultipartFile> photos) {
        //feedDto = null 은 @RequestBody 가 이미 체크하기에 한번 더 할 필요는 없어 보입니다.
        if (photos != null && photos.size() > 4) {
            return ResponseEntity.badRequest().body("4개 이상의 사진");
        }

         Feed feed = feedService.insert(feedDto);

    if (photos != null) {
        try {

            photoService.uploadPhotos(photos, feed);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("사진 업로드 에러");
        }
    }

    return ResponseEntity.ok("사진 업로드 완료");
    }

    @PostMapping("/{id}/photos")
    public ResponseEntity<?> updatePhotos(@PathVariable("id") Long feedId,
                                               @RequestParam("photos") List<MultipartFile> photos) {

        if (photos.size() > 4) {
            return ResponseEntity.badRequest().body("4개 이상의 사진");
        }

        Optional<FeedDTO> feedDto = feedService.read(feedId);

        if (feedDto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Feed feed = feedService.dtoToEntity(feedDto.get());

        try {
            photoService.uploadPhotos(photos, feed);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("사진 업로드 에러");
        }

        return ResponseEntity.ok("사진 업로드 성공");
    }

    @DeleteMapping("/{id}/photos")
    public ResponseEntity<?> deletePhotos(@PathVariable("id") Long feedId) {

        Optional<FeedDTO> feedDto = feedService.read(feedId);

        if (feedDto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Feed feed = feedService.dtoToEntity(feedDto.get());

        photoService.deletePhotosByFeed(feed);

        return ResponseEntity.ok("사진 삭제");
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
