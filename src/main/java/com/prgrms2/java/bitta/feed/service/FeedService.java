package com.prgrms2.java.bitta.feed.service;

import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.feed.entity.Feed;

import java.util.List;
import java.util.Optional;

public interface FeedService {
    Optional<FeedDTO> read(Long id);

    List<FeedDTO> readAll();

    String insert(FeedDTO feedDto);

    Optional<FeedDTO> update(FeedDTO feedDto);

    boolean delete(Long id);

    default Feed dtoToEntity(FeedDTO feedDto) {
        // 회원 아이디는 UserService 에서 할당해야 합니다.
        return Feed.builder()
                .feedId(feedDto.getFeedId())
                .title(feedDto.getTitle())
                .content(feedDto.getContent())
                .createdAt(feedDto.getCreatedAt())
                .build();
    }

    default FeedDTO entityToDto(Feed feed) {
        return FeedDTO.builder()
                .feedId(feed.getFeedId())
                .title(feed.getTitle())
                .content(feed.getContent())
                .createdAt(feed.getCreatedAt())
                .memberId(feed.getMember().getMemberId())
                .build();
    }
}
