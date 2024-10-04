package com.prgrms2.java.bitta.photo.service;

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

    @Override
    public void delete(Long feedId) {
        List<String> photoUrls = photoRepository.findPhotoUrlByFeedId(feedId);

        photoUrls.forEach(url -> {
            File file = new File(url);

            if (file.exists()) {
                file.delete();
            }
        });
    }
}
