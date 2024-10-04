package com.prgrms2.java.bitta.photo.repository;

import com.prgrms2.java.bitta.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Query("SELECT p.photoUrl FROM Photo p WHERE p.feed.id = :feedId")
    List<String> findPhotoUrlByFeedId(@Param("feedId") Long feedId);

    void deleteByPhotoUrl(String photoUrl);
}
