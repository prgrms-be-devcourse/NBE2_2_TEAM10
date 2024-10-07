package com.prgrms2.java.bitta.media.service;

import com.prgrms2.java.bitta.media.dto.MediaDto;
import com.prgrms2.java.bitta.media.entity.Media;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {
    void upload(List<MultipartFile> files, Long feedId);

    void delete(List<MediaDto> mediaDtos);

    void delete(Long feedId);

    List<Media> convertDTOs(List<MediaDto> mediaDTOs);

    List<MediaDto> convertEntities(List<Media> medias);
}