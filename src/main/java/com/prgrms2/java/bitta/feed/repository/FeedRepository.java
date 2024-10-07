package com.prgrms2.java.bitta.feed.repository;

import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    @Query("SELECT f FROM Feed f WHERE f.member.username LIKE %:username%")
    Page<Feed> findAllLikeUsernameOrderByIdDesc(@Param("username") String username, Pageable pageable);

    @Query("SELECT f FROM Feed f WHERE f.title LIKE %:title%")
    Page<Feed> findAllLikeTitleOrderByIdDesc(@Param("title") String title, Pageable pageable);

    @Query("SELECT f FROM Feed f WHERE f.member.username LIKE %:username% AND f.title LIKE %:title%")
    Page<Feed> findAllLikeUsernameAndTitleOrderByIdDesc(@Param("username") String username, @Param("title") String title, Pageable pageable);

    Page<Feed> findAllByOrderByIdDesc(Pageable pageable);

    @Modifying
    @Query("DELETE FROM Feed f WHERE f.id = :id")
    int deleteByIdAndReturnCount(@Param("id") Long id);
}
