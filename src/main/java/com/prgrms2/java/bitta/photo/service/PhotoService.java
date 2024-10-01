package com.prgrms2.java.bitta.photo.service;

import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.photo.entity.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotoService {
    List<Photo> uploadPhotos(List<MultipartFile> files, Feed feed) throws IOException;
    void deletePhotosByFeed(Feed feed);
}
