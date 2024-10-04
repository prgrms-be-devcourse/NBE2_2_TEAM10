package com.prgrms2.java.bitta.video.service;

import com.prgrms2.java.bitta.video.entity.Video;
import com.prgrms2.java.bitta.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;

    @Value("${file.root.path}")
    private String fileRootPath;

    @Override
    @Transactional
    public Video upload(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filepath = fileRootPath + "uploads/videos/" + filename;

        file.transferTo(new File(filepath));

        return Video.builder()
                .videoUrl(filepath)
                .fileSize(file.getSize())
                .build();
    }

    @Override
    @Transactional
    public void delete(String filepath) {
        File file = new File(filepath);

        if (file.delete()) {
            videoRepository.deleteByVideoUrl(filepath);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void delete(Long feedId) {
        List<String> videoUrls = videoRepository.findVideoUrlByFeedId(feedId);

        videoUrls.forEach(url -> {
            File file = new File(url);

            if (file.exists()) {
                file.delete();
            }
        });
    }
}
