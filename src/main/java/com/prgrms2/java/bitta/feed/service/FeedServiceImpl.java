package com.prgrms2.java.bitta.feed.service;

import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.feed.exception.FeedException;
import com.prgrms2.java.bitta.feed.repository.FeedRepository;
import com.prgrms2.java.bitta.member.dto.MemberProvider;
import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.photo.entity.Photo;
import com.prgrms2.java.bitta.photo.service.PhotoService;
import com.prgrms2.java.bitta.video.entity.Video;
import com.prgrms2.java.bitta.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {
    private final FeedRepository feedRepository;

    private final PhotoService photoService;

    private final VideoService videoService;

    private final MemberProvider memberProvider;

    @Override
    @Transactional(readOnly = true)
    public FeedDTO read(Long id) {
        Feed feed = feedRepository.findById(id)
                .orElseThrow(FeedException.CANNOT_FOUND::get);

        return entityToDto(feed);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedDTO> readAll() {
        List<Feed> feeds = feedRepository.findAll();

        if (feeds.isEmpty()) {
            throw FeedException.CANNOT_FOUND.get();
        }

        return feeds.stream().map(this::entityToDto).toList();
    }

    @Override
    public List<FeedDTO> readAll(Member member) {
        List<Feed> feeds = feedRepository.findAllByMember(member);

        if (feeds.isEmpty()) {
            return null;
        }

        return feeds.stream().map(this::entityToDto).toList();
    }

    @Override
    @Transactional
    public void insert(FeedDTO feedDTO) {
        if (feedDTO.getId() != null) {
            throw FeedException.BAD_REQUEST.get();
        }

        Feed feed = dtoToEntity(feedDTO);

        feedRepository.save(feed);
    }

    @Override
    @Transactional
    public void update(FeedDTO feedDTO) {
        Feed feed = feedRepository.findById(feedDTO.getId())
                .orElseThrow(FeedException.CANNOT_FOUND::get);

        feed.setTitle(feedDTO.getTitle());
        feed.setContent(feedDTO.getContent());

        feedRepository.save(feed);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (feedRepository.deleteByIdAndReturnCount(id) == 0) {
            throw FeedException.CANNOT_DELETE.get();
        }
    }

    //photo and video
    @Override
    @Transactional
    public void addPhotosToFeed(Long feedId, List<MultipartFile> photos) throws IOException {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(FeedException.CANNOT_FOUND::get);
        photoService.uploadPhotos(photos, feed);
    }

    @Override
    @Transactional
    public void addVideosToFeed(Long feedId, List<MultipartFile> videos) throws IOException {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(FeedException.CANNOT_FOUND::get);
        videoService.uploadVideos(videos, feed);
    }


    private Feed dtoToEntity(FeedDTO feedDto) {
        return Feed.builder()
                .id(feedDto.getId())
                .title(feedDto.getTitle())
                .content(feedDto.getContent())
                .createdAt(feedDto.getCreatedAt())
                .member(memberProvider.getById(feedDto.getMemberId()))
                .build();
    }

    private FeedDTO entityToDto(Feed feed) {
        return FeedDTO.builder()
                .id(feed.getId())
                .title(feed.getTitle())
                .content(feed.getContent())
                .createdAt(feed.getCreatedAt())
                .id(feed.getMember().getId())
                .photoUrls(feed.getPhotos().stream()
                        .map(Photo::getPhotoUrl).toList())
                .videoUrls(feed.getVideos().stream()
                        .map(Video::getVideoUrl).toList())
                .build();
    }
}