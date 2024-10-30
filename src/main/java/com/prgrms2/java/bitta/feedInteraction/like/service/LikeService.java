package com.prgrms2.java.bitta.feedInteraction.like.service;

import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.feed.service.FeedService;
import com.prgrms2.java.bitta.feedInteraction.like.entity.Like;
import com.prgrms2.java.bitta.feedInteraction.like.repository.LikeRepository;
import com.prgrms2.java.bitta.member.dto.MemberResponseDto;
import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    @Transactional
    public boolean toggleLike(Long feedId, Long memberId) {
        return likeRepository.findByFeedAndMember(feedId, memberId)
                .map(like -> {
                    like.setLiked(!like.isLiked());
                    return like.isLiked();
                })
                .orElseGet(() -> {
                    Like newLike = Like.builder()
                            .feedId(feedId)
                            .memberId(memberId)
                            .liked(true)
                            .build();
                    likeRepository.save(newLike);
                    return true;
                });
    }

    @Transactional(readOnly = true)
    public long getLikeCount(Long feedId) {
        return likeRepository.countByFeedIdandLiked(feedId);
    }


}
