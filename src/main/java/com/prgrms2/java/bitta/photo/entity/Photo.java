package com.prgrms2.java.bitta.photo.entity;

import com.prgrms2.java.bitta.user.entity.User;
import com.prgrms2.java.bitta.feed.entity.Feed;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long photoId;

    private Long fileSize;

    private String photoUrl;

    //이곳에 있던 user 필드 삭제. feed 가 이미 user 에게 연결되어 있고, 사진은 feed 로만 연결되어도 문제 없을거 같아요

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime uploadedAt;
}
