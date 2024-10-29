package com.prgrms2.java.bitta.feed.service;

import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.feed.dto.FeedRequestDto;
import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.feed.exception.FeedException;
import com.prgrms2.java.bitta.feed.repository.FeedRepository;
import com.prgrms2.java.bitta.media.dto.MediaDto;
import com.prgrms2.java.bitta.media.entity.Media;
import com.prgrms2.java.bitta.media.service.MediaService;
import com.prgrms2.java.bitta.member.service.MemberProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {
    private final FeedRepository feedRepository;

    private final MediaService mediaService;

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
    public Page<FeedDTO> readAll(Pageable pageable, String username, String title) {
        Page<Feed> feeds = null;

        if (StringUtils.hasText(username) && StringUtils.hasText(title)) {
            feeds = feedRepository.findAllLikeUsernameAndTitleOrderByIdDesc(username, title, pageable);
        } else if (StringUtils.hasText(username)) {
            feeds = feedRepository.findAllLikeUsernameOrderByIdDesc(username, pageable);
        } else if (StringUtils.hasText(title)) {
            feeds = feedRepository.findAllLikeTitleOrderByIdDesc(title, pageable);
        } else {
            feeds = feedRepository.findAllByOrderByIdDesc(pageable);
        }

        return Optional.ofNullable(feeds)
                .filter(f -> !f.isEmpty())
                .map(f -> f.map(this::entityToDto))
                .orElseThrow(FeedException.CANNOT_FOUND::get);
    }

    @Override
    @Transactional
    public void insert(FeedDTO feedDTO, List<MultipartFile> files) {
        if (feedDTO.getId() != null) {
            throw FeedException.BAD_REQUEST.get();
        }

        Feed feed = dtoToEntity(feedDTO);

        feed = feedRepository.save(feed);

        mediaService.uploads(files, feed.getId());
    }

    @Override
    @Transactional
    public void update(FeedRequestDto.Modify feedDto, List<MultipartFile> filesToUpload, List<String> filesToDeletes) {
        Feed feed = feedRepository.findById(feedDto.getId())
                .orElseThrow(FeedException.CANNOT_FOUND::get);

        feed.setTitle(feedDto.getTitle());
        feed.setContent(feedDto.getContent());

        if (filesToDeletes != null) {
            List<Media> deleteMedias = mediaService.getMedias(filesToDeletes);

            mediaService.deleteExistFiles(deleteMedias);
        }

        feed.clearMedias();

        mediaService.uploads(filesToUpload, feedDto.getId());
        
        feedRepository.save(feed);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Feed feed = feedRepository.findById(id)
                .orElseThrow(FeedException.CANNOT_FOUND::get);

        if (feed.getMedias() != null) {
            mediaService.deleteAll(feed.getMedias());
        }

        feed.setMember(null);
        feedRepository.delete(feed);
    }

    @Override
    public boolean checkAuthority(Long feedId, String username) {
        return feedRepository.existsByIdAndMember_Username(feedId, username);
    }

    private Feed dtoToEntity(FeedDTO feedDto) {
        return Feed.builder()
                .id(feedDto.getId())
                .title(feedDto.getTitle())
                .content(feedDto.getContent())
                .createdAt(feedDto.getCreatedAt())
                .member(memberProvider.getById(feedDto.getMemberId()))
                .medias(mediaService.getMedias(feedDto.getMedias()))
                .build();
    }

    private FeedDTO entityToDto(Feed feed) {
        return FeedDTO.builder()
                .id(feed.getId())
                .title(feed.getTitle())
                .content(feed.getContent())
                .createdAt(feed.getCreatedAt())
                .memberId(feed.getMember().getId())
                .medias(mediaService.getUrls(feed.getMedias()))
                .build();
    }
}