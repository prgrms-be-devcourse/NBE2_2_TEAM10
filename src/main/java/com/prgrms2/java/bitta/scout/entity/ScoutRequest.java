package com.prgrms2.java.bitta.scout.entity;

import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ScoutRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;  // The feed for which the scout request is made.

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;  // The member who is sending the scout request.

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;  // The member who owns the feed and will receive the request.

    @Lob
    private String description;  // A description the sender can attach to the request.

    @Column(updatable = false)
    private LocalDateTime sentAt;  // Timestamp when the request was sent.
}