package com.prgrms2.java.bitta.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "application")
@EntityListeners(AuditingEntityListener.class)
public class PostApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    // Application은 특정 JobPost에 속함 (N:1 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_post_id", nullable = false)
    private JobPost jobPost;

    // Application은 특정 User의 신청 정보 (N:1 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // User의 Feed 목록도 가져옴 (1:N 관계)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Feed> feeds;

    @CreatedDate
    private LocalDateTime appliedAt;
}
