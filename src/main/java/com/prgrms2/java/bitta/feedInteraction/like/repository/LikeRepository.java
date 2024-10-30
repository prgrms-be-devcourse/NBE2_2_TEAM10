package com.prgrms2.java.bitta.feedInteraction.like.repository;

import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.member.dto.MemberDTO;
import com.prgrms2.java.bitta.member.dto.MemberResponseDto;
import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.feedInteraction.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByFeedAndMember (Long feedId, Long memberId);
    Long countByFeedIdandLiked(Long feedId);

}
