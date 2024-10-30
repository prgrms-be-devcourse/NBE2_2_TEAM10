package com.prgrms2.java.bitta.feedInteraction.viewCount.service;

import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.feed.repository.FeedRepository;
import com.prgrms2.java.bitta.feedInteraction.viewCount.entity.ViewCount;
import com.prgrms2.java.bitta.feedInteraction.viewCount.repository.ViewCountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ViewCountService {
    private final ViewCountRepository viewCountRepository;
    private final FeedRepository feedRepository;

    @Transactional
    public void addView(Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new EntityNotFoundException("Feed 를 찾을 수 없습니다"));

        ViewCount viewCount = viewCountRepository.findByFeed(feed).orElseGet(() -> {
            ViewCount newViewCount = new ViewCount();
            newViewCount.setFeed(feed);
            newViewCount.setCount(0L);
            return viewCountRepository.save(newViewCount);
        });

        viewCount.setCount(viewCount.getCount() + 1);
        viewCountRepository.save(viewCount);
    }
        @Transactional(readOnly = true)
        public Long getViewCount(Long feedId) {
            Feed feed = feedRepository.findById(feedId).orElseThrow(() ->new EntityNotFoundException("Feed 를 찾을 수 없습니다"));

            return viewCountRepository.findByFeed(feed).map(ViewCount::getCount).orElse(0L);
        }
}

