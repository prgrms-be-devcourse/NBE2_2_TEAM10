package com.prgrms2.java.bitta.feed.repository;

import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    @Query("SELECT f FROM Feed f WHERE f.member = :member")
    List<Feed> findAllByMember(@Param("member") Member member);

    @Modifying
    @Query("DELETE FROM Feed f WHERE f.id = :id")
    Long deleteByIdAndReturnCount(@Param("id") Long id);
}
