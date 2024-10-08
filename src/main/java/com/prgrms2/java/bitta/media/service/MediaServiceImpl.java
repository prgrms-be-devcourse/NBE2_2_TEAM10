package com.prgrms2.java.bitta.media.service;

import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.feed.service.FeedProvider;
import com.prgrms2.java.bitta.jobpost.entity.JobPost;
import com.prgrms2.java.bitta.jobpost.util.JobPostProvider;
import com.prgrms2.java.bitta.media.dto.MediaDto;
import com.prgrms2.java.bitta.media.entity.MediaCategory;
import com.prgrms2.java.bitta.media.entity.Media;
import com.prgrms2.java.bitta.media.exception.MediaException;
import com.prgrms2.java.bitta.media.repository.MediaRepository;

import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.member.service.MemberProvider;
import lombok.RequiredArgsConstructor;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {
    private final MediaRepository mediaRepository;

    private final FeedProvider feedProvider;

    private final MemberProvider memberProvider;

    private final JobPostProvider jobPostProvider;

    @Value("${file.root.path}")
    private String rootPath;

    @Override
    @Transactional
    public void uploads(List<MultipartFile> multipartFiles, Long feedId) {
        List<MediaCategory> categories = checkFileType(multipartFiles);
        List<Media> medias = new ArrayList<>();
        Feed feed = feedProvider.getById(feedId);

        for (int i = 0; i < multipartFiles.size(); i++) {
            MultipartFile multipartFile = multipartFiles.get(i);

            String filename = UUID.randomUUID().toString();
            String extension = "." + StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());

            try {
                multipartFile.transferTo(Paths.get(rootPath, filename + extension));
            } catch (IOException e) {
                throw MediaException.INTERNAL_ERROR.get();
            }

            medias.add(Media.builder()
                    .filename(filename)
                    .extension(extension)
                    .size(multipartFile.getSize())
                    .type(categories.get(i))
                    .feed(feed)
                    .build());
        }

        mediaRepository.saveAll(medias);
    }

    @Override
    public void upload(MultipartFile multipartFile, Long memberId, Long jobPostId) {
        String filename = UUID.randomUUID().toString();
        String extension = "." + StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        String filepath = rootPath + filename + extension;
        MediaCategory category = checkFileType(multipartFile);

        File file = new File(filepath);
        Member member = null;
        JobPost jobPost = null;

        try {
            if (memberId != null && jobPostId != null) {
                throw MediaException.BAD_REQUEST.get();
            }

            if (memberId != null) {
                member = memberProvider.getById(memberId);
                multipartFile.transferTo(file);
            }

            if (jobPostId != null) {
                jobPost = jobPostProvider.getById(jobPostId);
                Thumbnails.of(multipartFile.getInputStream())
                        .size(200, 200)
                        .keepAspectRatio(true)
                        .toFile(file);
            }
        } catch (IOException e) {
            throw MediaException.INTERNAL_ERROR.get();
        }

        Media media = Media.builder()
                .filename(filename)
                .extension(extension)
                .size(multipartFile.getSize())
                .type(category)
                .member(member)
                .jobPost(jobPost)
                .build();

        mediaRepository.save(media);
    }

    @Override
    @Transactional(readOnly = true)
    public void delete(Media media) {
        delete(entityToDto(media));
    }

    @Override
    @Transactional(readOnly = true)
    public void delete(Long feedId) {
        List<Media> medias = mediaRepository.findAllByFeedId(feedId);

        medias.forEach(media -> {
            String filepath = rootPath + media.getFilename() + media.getExtension();

            File file = new File(filepath);

            if (file.exists()) {
                file.delete();
            } else {
                throw MediaException.NOT_FOUND.get();
            }
        });
    }

    @Override
    public void delete(List<MediaDto> mediaDTOs) {
        mediaDTOs.forEach(this::delete);
    }

    @Override
    public void delete(MediaDto mediaDto) {
        String filepath = rootPath + mediaDto.getFilename() + mediaDto.getExtension();

        File file = new File(filepath);

        if (file.exists()) {
            file.delete();
        } else {
            throw MediaException.NOT_FOUND.get();
        }
    }

    @Override
    public String getMediaUrl(Media media) {
        return rootPath + media.getFilename() + media.getExtension();
    }

    @Override
    public Media getMedia(String mediaUrl) {
        String filename = mediaUrl.substring(rootPath.length())
                .substring(0, mediaUrl.indexOf("."));

        return mediaRepository.findByFilename(filename).orElse(null);
    }

    @Override
    public List<Media> convertDTOs(List<MediaDto> mediaDTOs) {
        return mediaDTOs.stream().map(this::dtoToEntity).toList();
    }

    @Override
    public List<MediaDto> convertEntities(List<Media> medias) {
        return medias.stream().map(this::entityToDto).toList();
    }

    private List<MediaCategory> checkFileType(List<MultipartFile> multipartFiles) {
        List<MediaCategory> categories = new ArrayList<>();

        multipartFiles.forEach(multipartFile
                -> categories.add(checkFileType(multipartFile)));

        return categories;
    }

    private MediaCategory checkFileType(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();

        if (contentType.matches("image/(jpeg|png|gif|bmp|webp|svg\\+xml)")) {
            return MediaCategory.IMAGE;
        }

        if (contentType.matches("video/(mp4|webm|ogg|x-msvideo|x-matroska)")) {
            return MediaCategory.VIDEO;
        }

        throw MediaException.INVALID_FORMAT.get();
    }

    private Media dtoToEntity(MediaDto mediaDto) {
        Long feedId = mediaDto.getFeedId();
        Long memberId = mediaDto.getMemberId();
        Long jobPostId = mediaDto.getJobPostId();

        return Media.builder()
                .id(mediaDto.getId())
                .filename(mediaDto.getFilename())
                .extension(mediaDto.getExtension())
                .size(mediaDto.getSize())
                .type(mediaDto.getType())
                .feed(feedId != null ? feedProvider.getById(feedId) : null)
                .member(memberId != null ? memberProvider.getById(memberId) : null)
                .jobPost(jobPostId != null ? jobPostProvider.getById(jobPostId) : null)
                .createdAt(mediaDto.getCreatedAt())
                .updatedAt(mediaDto.getUpdatedAt())
                .build();
    }

    private MediaDto entityToDto(Media media) {
        Feed feed = media.getFeed();
        Member member = media.getMember();
        JobPost jobPost = media.getJobPost();

        return MediaDto.builder()
                .id(media.getId())
                .filename(media.getFilename())
                .extension(media.getExtension())
                .size(media.getSize())
                .type(media.getType())
                .feedId(feed != null ? feed.getId() : null)
                .memberId(member != null ? member.getId() : null)
                .jobPostId(jobPost != null ? jobPost.getId() : null)
                .createdAt(media.getCreatedAt())
                .updatedAt(media.getUpdatedAt())
                .build();
    }
}
