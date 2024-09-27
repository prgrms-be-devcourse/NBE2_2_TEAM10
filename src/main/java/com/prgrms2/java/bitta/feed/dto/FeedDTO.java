package com.prgrms2.java.bitta.feed.dto;

import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedDTO {
    private Long feedId;
    private String title;
    private String content;
    private Long memberId;
    private LocalDateTime createdAt;

    public FeedDTO(Feed feed) {
        this.feedId = feed.getFeedId();
        this.title = feed.getTitle();
        this.content = feed.getContent();
        this.memberId = feed.getMember().getMemberId();
        this.createdAt = feed.getCreatedAt();
    }

    public Feed toEntity(Member member) {
        Feed feed = Feed.builder()
                .title(title)         // title을 넘겨줌
                .content(content)     // content를 넘겨줌
                .member(member)       // Member 객체를 넘겨줌
                .createdAt(createdAt) // createdAt을 넘겨줌
                .build();
        return feed;
    }
}
