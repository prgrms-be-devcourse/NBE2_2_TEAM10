package com.prgrms2.java.bitta.member.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProfileImageService {

    @Value("${file.root.path}")
    private String fileRootPath;

    private static final String PROFILE_DIR = "/uploads/profile_images/";

    public String getDefaultProfileImgPath() {
        return fileRootPath + PROFILE_DIR + "default_avatar.png";
    }

    public String saveProfileImage(MultipartFile profileImage) throws IOException {
        String directory = fileRootPath + PROFILE_DIR;
        Path directoryPath = Paths.get(directory);

        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        String originalFilename = profileImage.getOriginalFilename();
        Path filePath = directoryPath.resolve(originalFilename);
        profileImage.transferTo(filePath.toFile());

        return filePath.toString();
    }

    public void deleteProfileImage(String profileImgPath) {
        if (profileImgPath != null && !profileImgPath.isBlank()) {
            File profileImgFile = new File(profileImgPath);
            File thumbnailFile = getThumbnailFile(profileImgPath);

            if (profileImgFile.exists() && profileImgFile.isFile()) {
                profileImgFile.delete();
            }

            if (thumbnailFile.exists() && thumbnailFile.isFile()) {
                thumbnailFile.delete();
            }
        }
    }

    public File getThumbnailFile(String profileImg) {
        String thumbnailImgPath = profileImg.replace("profile_images", "profile_images/thumbnail");
        String thumbnailFileName = "thumb_" + Paths.get(profileImg).getFileName().toString();
        return new File(thumbnailImgPath.replace(Paths.get(profileImg).getFileName().toString(), thumbnailFileName));
    }

    public String createThumbnail(String imagePath) throws IOException {
        String thumbnailDirectory = fileRootPath + PROFILE_DIR + "thumbnail/";
        Path thumbnailPath = Paths.get(thumbnailDirectory);

        if (!Files.exists(thumbnailPath)) {
            Files.createDirectories(thumbnailPath);
        }

        String originalFileName = Paths.get(imagePath).getFileName().toString();
        String thumbnailFileName = "thumb_" + originalFileName;
        Path thumbnailFilePath = thumbnailPath.resolve(thumbnailFileName);

        Thumbnails.of(new File(imagePath))
                .size(200, 200)
                .keepAspectRatio(true)
                .toFile(thumbnailFilePath.toFile());

        return thumbnailFilePath.toString();
    }
}
