package com.prgrms2.java.bitta.photo.service;

import com.prgrms2.java.bitta.photo.entity.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PhotoService {
    Photo upload(MultipartFile file) throws IOException;

    void delete(String filepath);

    void delete(Long feedId);
}
