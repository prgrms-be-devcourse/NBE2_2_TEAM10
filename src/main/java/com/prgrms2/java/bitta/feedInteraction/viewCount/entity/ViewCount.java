package com.prgrms2.java.bitta.feedInteraction.viewCount.entity;


import com.prgrms2.java.bitta.feed.entity.Feed;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    Feed feed;

    @Column (name = "count", nullable = false)
    private long count = 0L;

}
