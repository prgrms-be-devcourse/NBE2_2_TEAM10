package com.prgrms2.java.bitta.video.repository;

import com.prgrms2.java.bitta.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    @Query("SELECT v.videoUrl FROM Video v WHERE v.feed.id = :feedId")
    List<String> findVideoUrlByFeedId(@Param("feedId") Long feedId);

    void deleteByVideoUrl(String videoUrl);
}
