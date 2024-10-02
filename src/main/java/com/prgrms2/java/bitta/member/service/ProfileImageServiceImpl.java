package com.prgrms2.java.bitta.member.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProfileImageServiceImpl implements ProfileImageService {
    @Value("${file.root.path}")
    private String rootPath;

    @Value("${spring.servlet.multipart.max-file-size}")
    private long maxFileSize;

    @Override
    public String saveProfileImage(MultipartFile file) {
        // 파일 형식 및 크기 검증
        validateFile(file);

        try {
            // 저장할 디렉토리 확인 및 없을 시 생성
            String imageDirectory = rootPath + "profile/images/";
            File directory = new File(imageDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 파일 저장
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filepath = Paths.get(imageDirectory + filename);
            Files.write(filepath, file.getBytes());

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
            throw new IllegalArgumentException("지원되지 않는 파일 형식입니다. JPG 또는 PNG 파일만 허용됩니다.");
        }
        if (file.getSize() > maxFileSize) {  // 파일 크기 검증
            throw new IllegalArgumentException("파일 크기가 너무 큽니다. 최대 " + maxFileSize + " 이하의 파일만 업로드 가능합니다.");
        }
    }

    public void updateProfileImage(Long memberId, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String filename = memberId + "_" + file.getOriginalFilename();
            String imageDirectory = rootPath + "profile/images/";
            Path filePath = Paths.get(imageDirectory + filename);
            Files.write(filePath, file.getBytes());
        }
    }
}
