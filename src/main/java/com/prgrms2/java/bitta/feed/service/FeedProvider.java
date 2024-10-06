package com.prgrms2.java.bitta.feed.service;

import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.feed.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedProvider {
    private final FeedRepository feedRepository;

    @Transactional(readOnly = true)
    public Feed getById(Long id) {
        return feedRepository.findById(id).orElse(null);
    }
}
