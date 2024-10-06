package com.prgrms2.java.bitta.media.service;

import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.feed.service.FeedProvider;
import com.prgrms2.java.bitta.media.dto.MediaDto;
import com.prgrms2.java.bitta.media.entity.MediaCategory;
import com.prgrms2.java.bitta.media.entity.Media;
import com.prgrms2.java.bitta.media.exception.MediaFileException;
import com.prgrms2.java.bitta.media.repository.MediaRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {
    private final MediaRepository mediaRepository;

    private final FeedProvider feedProvider;

    @Value("${file.root.path}")
    private String rootPath;

    @Override
    public void upload(List<MultipartFile> files, Long feedId) {
        List<MediaCategory> categories = checkFileType(files);
        List<Media> media = new ArrayList<>();
        Feed feed = feedProvider.getById(feedId);

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);

            String filename = UUID.randomUUID().toString();
            String extension = "." + StringUtils.getFilenameExtension(file.getOriginalFilename());

            try {
                file.transferTo(new File(rootPath + filename + extension));
            } catch (IOException e) {
                throw MediaFileException.INTERNAL_ERROR.get();
            }

            media.add(Media.builder()
                    .filename(filename)
                    .extension(extension)
                    .size(file.getSize())
                    .type(categories.get(i))
                    .feed(feed)
                    .build());
        }

        mediaRepository.saveAll(media);
    }

    @Override
    public void delete(List<MediaDto> mediaDtos) {
        mediaDtos.forEach(dto -> {
            String filepath = rootPath + dto.getFilename() + dto.getExtension();

            File file = new File(filepath);

            if (file.exists()) {
                file.delete();
            } else {
                throw MediaFileException.NOT_FOUND.get();
            }
        });
    }

    @Override
    public void delete(Long feedId) {
        List<Media> medias = mediaRepository.findAllByFeedId(feedId);

        medias.forEach(media -> {
            String filepath = rootPath + media.getFilename() + media.getExtension();

            File file = new File(filepath);

            if (file.exists()) {
                file.delete();
            } else {
                throw MediaFileException.NOT_FOUND.get();
            }
        });
    }

    private List<MediaCategory> checkFileType(List<MultipartFile> files) {
        List<MediaCategory> categories = new ArrayList<>();

        files.forEach(file -> {
            String contentType = file.getContentType();

            if (contentType.startsWith("image/")) {
                categories.add(MediaCategory.IMAGE);
            } else if (contentType.startsWith("video/")) {
                categories.add(MediaCategory.VIDEO);
            } else {
                throw MediaFileException.INVALID_FORMAT.get();
            }
        });

        return categories;
    }

    @Override
    public List<Media> convertDTOs(List<MediaDto> mediaDTOs) {
        return mediaDTOs.stream().map(this::dtoToEntity).toList();
    }

    @Override
    public List<MediaDto> convertEntities(List<Media> medias) {
        return medias.stream().map(this::entityToDto).toList();
    }

    private Media dtoToEntity(MediaDto mediaDto) {
        return Media.builder()
                .id(mediaDto.getId())
                .filename(mediaDto.getFilename())
                .extension(mediaDto.getExtension())
                .size(mediaDto.getSize())
                .type(mediaDto.getType())
                .feed(feedProvider.getById(mediaDto.getFeedId()))
                .createdAt(mediaDto.getCreatedAt())
                .updatedAt(mediaDto.getUpdatedAt())
                .build();
    }

    private MediaDto entityToDto(Media media) {
        return MediaDto.builder()
                .id(media.getId())
                .filename(media.getFilename())
                .extension(media.getExtension())
                .size(media.getSize())
                .type(media.getType())
                .feedId(media.getFeed().getId())
                .createdAt(media.getCreatedAt())
                .updatedAt(media.getUpdatedAt())
                .build();
    }
}
