package com.prgrms2.java.bitta.video.service;

import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.video.entity.Video;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VideoService {
    Video upload(MultipartFile file) throws IOException;

    void delete(String filepath);

    List<Video> uploadVideos(List<MultipartFile> files, Feed feed) throws IOException;

    void deleteVideosByFeed(Feed feed);
}

