package com.prgrms2.java.bitta.feed.service;

import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.media.dto.MediaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedService {
    FeedDTO read(Long id);

    Page<FeedDTO> readAll(Pageable pageable, String username, String title);

    void insert(FeedDTO feedDto, List<MultipartFile> files);

    void update(FeedDTO feedDto, List<MultipartFile> filesToUpload, List<MediaDto> filesToDelete);

    void delete(Long id);
}
