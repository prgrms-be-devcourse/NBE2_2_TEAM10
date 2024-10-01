package com.prgrms2.java.bitta.feed.entity;

import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.photo.entity.Photo;
import com.prgrms2.java.bitta.video.entity.Video;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Feed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();

    // 비디오 리스트
    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Video> videos = new ArrayList<>();

    // 사진 추가 메서드
    public void addPhoto(Photo photo) {
        this.photos.add(photo);
        photo.setFeed(this);
    }

    // 비디오 추가 메서드
    public void addVideo(Video video) {
        this.videos.add(video);
        video.setFeed(this);
    }
}
