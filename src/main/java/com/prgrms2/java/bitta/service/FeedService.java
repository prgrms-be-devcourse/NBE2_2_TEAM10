package com.prgrms2.java.bitta.service;

import com.prgrms2.java.bitta.dto.FeedDto;
import com.prgrms2.java.bitta.entity.Feed;

import java.util.List;
import java.util.Optional;

public interface FeedService {
    Optional<FeedDto> read(Long id);

    List<FeedDto> readAll();

    String insert(FeedDto feedDto);

    Optional<FeedDto> update(FeedDto feedDto);

    boolean delete(Long id);

    default Feed dtoToEntity(FeedDto feedDto) {
        // 회원 아이디는 UserService 에서 할당해야 합니다.
        return Feed.builder()
                .feedId(feedDto.getFeedId())
                .title(feedDto.getTitle())
                .content(feedDto.getContent())
                .createdAt(feedDto.getCreatedAt())
                .build();
    }

    default FeedDto entityToDto(Feed feed) {
        return FeedDto.builder()
                .feedId(feed.getFeedId())
                .title(feed.getTitle())
                .content(feed.getContent())
                .createdAt(feed.getCreatedAt())
                .userId(feed.getUser().getUserId())
                .build();
    }
}
