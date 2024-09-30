package com.prgrms2.java.bitta.feed.service;

import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.feed.exception.FeedException;
import com.prgrms2.java.bitta.feed.repository.FeedRepository;
import com.prgrms2.java.bitta.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {
    private final FeedRepository feedRepository;

    private final MemberService memberService;

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

    private Feed dtoToEntity(FeedDTO feedDto) {
        return Feed.builder()
                .id(feedDto.getId())
                .title(feedDto.getTitle())
                .content(feedDto.getContent())
                .createdAt(feedDto.getCreatedAt())
                .member(memberService.getByEmail(feedDto.getEmail()))
                .build();
    }

    private FeedDTO entityToDto(Feed feed) {
        return FeedDTO.builder()
                .id(feed.getId())
                .title(feed.getTitle())
                .content(feed.getContent())
                .createdAt(feed.getCreatedAt())
                .email(feed.getMember().getEmail())
                .build();
    }
}