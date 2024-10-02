package com.prgrms2.java.bitta.photo.service;

import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.photo.entity.Photo;
import com.prgrms2.java.bitta.photo.repository.PhotoRepository;
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
public class PhotoServiceImpl implements PhotoService{
    private final PhotoRepository photoRepository;

    @Value("${file.root.path}")
    private String fileRootPath;

    @Override
    @Transactional
    public Photo upload(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filepath = fileRootPath + "uploads/photos/" + filename;

        file.transferTo(new File(filepath));

        return Photo.builder()
                .photoUrl(filepath)
                .fileSize(file.getSize())
                .build();
    }

    @Override
    @Transactional
    public void delete(String filepath) {
        File file = new File(filepath);

        if (file.delete()) {
            photoRepository.deleteByPhotoUrl(filepath);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Transactional
    public List<Photo> uploadPhotos(List<MultipartFile> files, Feed feed) throws IOException {

        List<Photo> existingPhotos = photoRepository.findByFeed(feed);

        if (existingPhotos.size() + files.size() > 4) {
            throw new IllegalArgumentException("사진은 4만 업로드 할 수 있습니다");
        }

        for (MultipartFile file : files) {

            if (file.getSize() > 10 * 1024 * 1024) {
                throw new IllegalArgumentException("사진의 크기는 10mb 를 넘을 수 없습니다");
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = "uploads/photos/" + fileName;

            File dest = new File(filePath);
            file.transferTo(dest);

            Photo photo = Photo.builder()
                    .photoUrl(filePath)
                    .fileSize(file.getSize())
                    .feed(feed)
                    .build();

            photoRepository.save(photo);
        }
        return photoRepository.findByFeed(feed);
    }

    @Transactional
    public void deletePhotosByFeed(Feed feed) {
        List<Photo> photos = photoRepository.findByFeed(feed);

        photoRepository.deleteAll(photos);
    }
}
