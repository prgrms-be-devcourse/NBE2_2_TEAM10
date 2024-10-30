package com.prgrms2.java.bitta.feedInteraction.viewCount.repository;

import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.feedInteraction.viewCount.entity.ViewCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ViewCountRepository extends JpaRepository<ViewCount, Long> {
    Optional<ViewCount> findByFeed(Feed feed);

}
