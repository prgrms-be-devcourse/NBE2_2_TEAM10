package com.prgrms2.java.bitta.photo.service;

import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.photo.entity.Photo;
import com.prgrms2.java.bitta.photo.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService{
    private final PhotoRepository photoRepository;

    @Transactional
    public List<Photo> uploadPhotos(List<MultipartFile> files, Feed feed) throws IOException {
        for (MultipartFile file : files) {
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
