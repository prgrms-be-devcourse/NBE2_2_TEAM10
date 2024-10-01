package com.prgrms2.java.bitta.member.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProfileImageServiceImpl implements ProfileImageService {

    private final String IMAGE_DIRECTORY = "/path/to/profile/images/";
    private final long MAX_FILE_SIZE = 1_000_000; // 1MB 파일 크기 제한

    @Override
    public String saveProfileImage(MultipartFile file) {
        // 파일 형식 및 크기 검증
        validateFile(file);

        try {
            // 저장할 디렉토리 확인 및 없을 시 생성
            File directory = new File(IMAGE_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 파일 저장
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filepath = Paths.get(IMAGE_DIRECTORY + filename);
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

    // 파일 형식 및 크기 검증
    private void validateFile(MultipartFile file) {
        if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")) {
            throw new IllegalArgumentException("지원되지 않는 파일 형식입니다. JPG 또는 PNG 파일만 허용됩니다.");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("파일 크기가 너무 큽니다. 최대 1MB 이하의 파일만 업로드 가능합니다.");
        }
    }

    public void updateProfileImage(Long memberId, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String filename = memberId + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(IMAGE_DIRECTORY + filename);
            Files.write(filePath, file.getBytes());
        }
    }
}
