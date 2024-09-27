package com.prgrms2.java.bitta.photo.service;

import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.photo.entity.Photo;
import com.prgrms2.java.bitta.photo.repository.PhotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String filePath = " " + file.getOriginalFilename(); //file path. 자료 저장을 어디다 하냐에 바꾸면 됩니다.
        File dest = new File(filePath);
        file.transferTo(dest);

        return filePath;
    }

    public List<Photo> uploadPhotos(List<MultipartFile> files, Feed feed) throws IOException {

        deletePhotosByFeed(feed);

        return files.stream().map(file -> {
            try {
                String fileUrl = uploadFile(file);
                Photo photo = Photo.builder()
                        .photoUrl(fileUrl)
                        .fileSize(file.getSize())
                        .feed(feed)
                        .build();
                return photoRepository.save(photo);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload photo", e);
            }
        }).collect(Collectors.toList());
    }


    public void deletePhotosByFeed(Feed feed) {
        List<Photo> photos = photoRepository.findByFeed(feed);
        photoRepository.deleteAll(photos);
    }
}