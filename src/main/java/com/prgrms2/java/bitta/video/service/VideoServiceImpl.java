package com.prgrms2.java.bitta.video.service;

import com.prgrms2.java.bitta.feed.entity.Feed;
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

    @Transactional
    public List<Video> uploadVideos(List<MultipartFile> files, Feed feed) throws IOException {

        List<Video> existingVideos = videoRepository.findByFeed(feed);

        if (existingVideos.size() + files.size() > 1) {
            throw new IllegalArgumentException("비디오는 한개만 업로드 할 수 있습니다");
        }

        for (MultipartFile file : files) {

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = "uploads/videos/" + fileName;

            File dest = new File(filePath);
            file.transferTo(dest);

            Video video = Video.builder()
                    .videoUrl(filePath)
                    .fileSize(file.getSize())
                    .feed(feed)
                    .build();

            videoRepository.save(video);
        }
        return videoRepository.findByFeed(feed);
    }

    @Transactional
    public void deleteVideosByFeed(Feed feed) {
        List<Video> videos = videoRepository.findByFeed(feed);
        videoRepository.deleteAll(videos);
    }
}
