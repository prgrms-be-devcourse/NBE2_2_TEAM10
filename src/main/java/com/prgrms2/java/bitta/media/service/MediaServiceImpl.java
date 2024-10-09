package com.prgrms2.java.bitta.media.service;

import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.feed.service.FeedProvider;
import com.prgrms2.java.bitta.jobpost.entity.JobPost;
import com.prgrms2.java.bitta.jobpost.util.JobPostProvider;
import com.prgrms2.java.bitta.media.dto.MediaDto;
import com.prgrms2.java.bitta.media.entity.MediaCategory;
import com.prgrms2.java.bitta.media.entity.Media;
import com.prgrms2.java.bitta.media.exception.MediaException;
import com.prgrms2.java.bitta.media.exception.MediaTaskException;
import com.prgrms2.java.bitta.media.repository.MediaRepository;

import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.member.service.MemberProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
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

    private final S3Service s3Service;

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
            MediaCategory category = categories.get(i);
            try {
                String filepath = category + filename;
                s3Service.upload(multipartFile, filepath);
            } catch (MediaTaskException ignored) {

            }

            medias.add(Media.builder()
                    .filename(filename)
                    .extension(extension)
                    .size(multipartFile.getSize())
                    .type(category)
                    .feed(feed)
                    .build());
        }

        mediaRepository.saveAll(medias);
    }

    @Override
    public void upload(MultipartFile multipartFile, Long memberId, Long jobPostId) {
        MediaCategory category = checkFileType(multipartFile);

        String filename = UUID.randomUUID().toString();
        String extension = "." + StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        String filepath = category.name() + "/" + filename;

        Member member = null;
        JobPost jobPost = null;
        Media media = null;
        try {
            if (memberId != null && jobPostId != null) {
                throw MediaException.BAD_REQUEST.get();
            }

            if (memberId != null) {
                member = memberProvider.getById(memberId);
                media = member.getMedia();

                if (media == null) {
                    media = Media.builder()
                            .filename(filename)
                            .extension(extension)
                            .size(multipartFile.getSize())
                            .type(category)
                            .member(member)
                            .build();
                } else {
                    media.setFilename(filename);
                    media.setExtension(extension);
                    media.setSize(multipartFile.getSize());
                    media.setType(category);
                }

                mediaRepository.save(media);

                s3Service.uploadThumbnail(multipartFile, filepath);

                return;
            }

            if (jobPostId != null) {
                jobPost = jobPostProvider.getById(jobPostId);
                media = jobPost.getMedia();

                if (media == null) {
                    media = Media.builder()
                            .filename(filename)
                            .extension(extension)
                            .size(multipartFile.getSize())
                            .type(category)
                            .jobPost(jobPost)
                            .build();
                } else {
                    media.setFilename(filename);
                    media.setExtension(extension);
                    media.setSize(multipartFile.getSize());
                    media.setType(category);
                }

                mediaRepository.save(media);

                s3Service.upload(multipartFile, filepath);
            }
        } catch (MediaTaskException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteExistFile(Media media) {
        String filepath = checkFileType(media.getExtension()) + media.getFilename() + media.getExtension();

        try {
            s3Service.delete(filepath);
        } catch (MediaTaskException ignored) {

        }
    }


    @Override
    @Transactional(readOnly = true)
    public void delete(Long feedId) {
        List<Media> medias = mediaRepository.findAllByFeedId(feedId);

        medias.forEach(media -> {
            String filepath = checkFileType(media.getExtension()) + media.getFilename() + media.getExtension();

            try {
                s3Service.delete(filepath);
            } catch (MediaTaskException ignored) {

            }
        });
    }

    @Override
    public void delete(List<MediaDto> mediaDTOs) {
        mediaDTOs.forEach(this::delete);
    }

    @Override
    public void delete(MediaDto mediaDto) {
        String filepath = checkFileType(mediaDto.getExtension()) + mediaDto.getFilename() + mediaDto.getExtension();

        try {
            s3Service.delete(filepath);
        } catch (MediaTaskException ignored) {

        }
    }

    @Override
    public String getUrl(Media media) {
        String filepath = checkFileType(media.getExtension()) + "/" + media.getFilename();

        System.out.println("URLURLURL : " + filepath);

        try {
            return s3Service.generatePreSignedUrl(filepath);
        } catch (MediaTaskException ignored) {

        }

        return null;
    }

    @Override
    public Media getMedia(String preSignedUrl) {
        String filename = extractFilename(preSignedUrl);

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
        String contentType = multipartFile.getContentType().toLowerCase();
        System.out.println(contentType);
        if (contentType.matches("image/(jpeg|png|gif|bmp|webp|svg\\+xml)")) {
            return MediaCategory.IMAGE;
        }

        if (contentType.matches("video/(mp4|webm|ogg|x-msvideo|x-matroska)")) {
            return MediaCategory.VIDEO;
        }

        throw MediaException.INVALID_FORMAT.get();
    }

    private MediaCategory checkFileType(String extension) {
        return extension.toLowerCase().matches("\\.(jpg|png|gif|bmp|webp|svg)$")
                ? MediaCategory.IMAGE : MediaCategory.VIDEO;
    }

    private String extractFilename(String preSignedUrl) {
        String uriPath = URI.create(preSignedUrl).getPath();

        return uriPath.substring(uriPath.lastIndexOf("/") + 1);
    }

    private String extractFilepath(String preSignedUrl) {
        String uriPath = URI.create(preSignedUrl).getPath();

        return uriPath.substring(uriPath.indexOf("/", 1) + 1);
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
