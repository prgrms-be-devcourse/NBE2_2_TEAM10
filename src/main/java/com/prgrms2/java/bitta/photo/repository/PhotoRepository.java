package com.prgrms2.java.bitta.photo.repository;

import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByFeed(Feed feed);
}
