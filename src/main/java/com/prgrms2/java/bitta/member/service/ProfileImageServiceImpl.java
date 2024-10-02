package com.prgrms2.java.bitta.member.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProfileImageServiceImpl implements ProfileImageService {
    @Value("${file.root.path}")
    private String rootPath;

    @Override
    public String saveProfileImage(MultipartFile file) {
        validateFile(file);

        try {
            String imageDirectory = rootPath + "profile/images/";
            File directory = new File(imageDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filepath = Paths.get(imageDirectory + filename);
            file.transferTo(filepath.toFile());

            return "/profile/images/" + filename;
        } catch (IOException e) {
            throw new IllegalStateException("프로필 이미지 저장에 실패했습니다.", e);
        }
    }

    @Override
    public String getDefaultProfileImage() {
        return "/images/default_avatar.png";
    }

    private void validateFile(MultipartFile file) {
        if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")) {
            throw new IllegalArgumentException("지원되지 않는 파일 형식입니다.");
        }
    }
}
