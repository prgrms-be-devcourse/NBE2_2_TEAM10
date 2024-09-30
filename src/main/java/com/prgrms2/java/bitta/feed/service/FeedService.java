package com.prgrms2.java.bitta.feed.service;

import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FeedService {
    FeedDTO read(Long id);

    List<FeedDTO> readAll();

    void insert(FeedDTO feedDto);

    void update(FeedDTO feedDto);

    void delete(Long id);

    //photo, video
    void addPhotosToFeed(Long feedId, List<MultipartFile> photos) throws IOException;
    void addVideosToFeed(Long feedId, List<MultipartFile> videos) throws IOException;
}
