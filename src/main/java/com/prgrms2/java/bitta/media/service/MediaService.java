package com.prgrms2.java.bitta.media.service;

import com.prgrms2.java.bitta.media.dto.MediaDto;
import com.prgrms2.java.bitta.media.entity.Media;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {
    void uploads(List<MultipartFile> files, Long feedId);

    void upload(MultipartFile file, Long memberId, Long jobPostId);

    void deleteExistFile(Media media);

    void deleteExistFiles(List<Media> medias);

    void delete(List<MediaDto> mediaDtos);

    void delete(MediaDto mediaDto);

    void deleteAll(List<Media> media);

    String getUrl(Media media);

    List<String> getUrls(List<Media> media);

    List<Media> getMedias(List<String> preSignedUrls);

    Media getMedia(String mediaUrl);

    List<Media> convertDTOs(List<MediaDto> mediaDTOs);

    List<MediaDto> convertEntities(List<Media> medias);
}