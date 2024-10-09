package com.prgrms2.java.bitta.media.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String generatePreSignedUrl(String filepath);

    void upload(MultipartFile multipartFile, String filepath);

    void uploadThumbnail(MultipartFile multipartFile, String filepath);

    void delete(String filepath);
}
