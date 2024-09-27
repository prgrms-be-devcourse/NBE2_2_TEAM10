package com.prgrms2.java.bitta.feed.repository;

import com.prgrms2.java.bitta.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    @Modifying
    @Query("DELETE FROM Feed f WHERE f.feedId = :feedId")
    Long deleteByFeedIdAndReturnCount(@Param("feedId") Long feedId);
}
