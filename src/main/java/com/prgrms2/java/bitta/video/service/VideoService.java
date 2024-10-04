package com.prgrms2.java.bitta.video.service;

import com.prgrms2.java.bitta.video.entity.Video;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VideoService {
    Video upload(MultipartFile file) throws IOException;

    void delete(String filepath);

    void delete(Long feedId);
}

