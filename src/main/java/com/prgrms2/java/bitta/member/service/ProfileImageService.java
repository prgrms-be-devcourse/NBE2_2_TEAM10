package com.prgrms2.java.bitta.member.service;

import org.springframework.web.multipart.MultipartFile;

public interface ProfileImageService {
    String saveProfileImage(MultipartFile file);
    String getDefaultProfileImage();
}
