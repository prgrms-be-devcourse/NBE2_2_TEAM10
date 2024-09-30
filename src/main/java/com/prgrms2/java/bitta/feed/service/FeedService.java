package com.prgrms2.java.bitta.feed.service;

import com.prgrms2.java.bitta.feed.dto.FeedDTO;

import java.util.List;

public interface FeedService {
    FeedDTO read(Long id);

    List<FeedDTO> readAll();

    void insert(FeedDTO feedDto);

    void update(FeedDTO feedDto);

    void delete(Long id);
}
