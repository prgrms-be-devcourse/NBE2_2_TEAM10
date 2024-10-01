package com.prgrms2.java.bitta.member.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProfileImageServiceImpl implements ProfileImageService {

    private final String IMAGE_DIRECTORY = "/path/to/profile/images/";

    @Override
    public String saveProfileImage(MultipartFile file) {
        try {
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filepath = Paths.get(IMAGE_DIRECTORY + filename);

            Files.write(filepath, file.getBytes());

            return "/profile/images/" + filename;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save profile image", e);
        }
    }

    @Override
    public String getDefaultProfileImage() {
        return "/images/default_avatar.png";
    }
}
