package com.prgrms2.java.bitta.media.service;

import com.prgrms2.java.bitta.media.dto.MediaDto;
import com.prgrms2.java.bitta.media.entity.Media;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {
    void uploads(List<MultipartFile> files, Long feedId);

    void upload(MultipartFile file, Long memberId, Long jobPostId);

    void deleteExistFile(Media media);

    void delete(List<MediaDto> mediaDtos);

    void delete(MediaDto mediaDto);

    void delete(Long feedId);

    String getUrl(Media media);

    Media getMedia(String mediaUrl);

    List<Media> convertDTOs(List<MediaDto> mediaDTOs);

    List<MediaDto> convertEntities(List<Media> medias);
}