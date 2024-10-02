package com.prgrms2.java.bitta.video.repository;

import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByFeed(Feed feed);

    void deleteByVideoUrl(String videoUrl);
}
