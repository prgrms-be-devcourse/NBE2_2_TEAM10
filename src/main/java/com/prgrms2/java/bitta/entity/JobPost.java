package com.prgrms2.java.bitta.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "job_post")
@EntityListeners(value = {AuditingEntityListener.class})
public class JobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobPostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;              // 게시글 작성자

    @Column(length = 100, nullable = false)
    private String title;           // 게시글 제목

    @Column(length = 500, nullable = false)
    private String description;     // 설명

    @Enumerated(EnumType.STRING)
    private Location location;      // 촬영 지역

    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;    // 급여 방식

    @CreatedDate
    private LocalDateTime createdAt;   // 게시글 생성일자

    @LastModifiedDate
    private LocalDateTime updatedAt;   // 게시글 수정일자

    private LocalDate startDate;   // 이벤트의 시작일
    private LocalDate endDate;     // 이벤트의 종료일

    @Transient
    private boolean isClosed;   // 게시글의 마감 여부

    // 이벤트 모집이 마감되면 true, 모집 중이면 false 반환
    public boolean isClosed() {
        return LocalDate.now().isAfter(this.endDate);
    }

    // 해당 게시글에 대한 신청 목록 가져야함 (임시로 application 엔티티 만듦)
    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Apply> apply = new ArrayList<>();

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeLocation(Location location) {
        this.location = location;
    }

    public void changePayStatus(PayStatus payStatus) {
        this.payStatus = payStatus;
    }
}
