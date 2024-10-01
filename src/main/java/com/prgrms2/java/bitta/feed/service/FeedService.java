package com.prgrms2.java.bitta.feed.service;

import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FeedService {
    FeedDTO read(Long id);

    List<FeedDTO> readAll();

    List<FeedDTO> readAll(Member member);

    void insert(FeedDTO feedDto, List<MultipartFile> files);

    void update(FeedDTO feedDto, List<MultipartFile> filesToUpload, List<String> filepathsToDelete);

    void delete(Long id);
}
