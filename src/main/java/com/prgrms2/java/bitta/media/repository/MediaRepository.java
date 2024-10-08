package com.prgrms2.java.bitta.media.repository;

import com.prgrms2.java.bitta.media.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    @Query("SELECT m FROM Media m WHERE m.feed.id = :feedId")
    List<Media> findAllByFeedId(@Param("feedId") Long feedId);

    Optional<Media> findByFilename(@Param("filename") String filename);
}
