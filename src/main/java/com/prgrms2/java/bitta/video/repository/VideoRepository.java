package com.prgrms2.java.bitta.video.repository;

import com.prgrms2.java.bitta.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
}
