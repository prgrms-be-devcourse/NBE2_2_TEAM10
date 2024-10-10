package com.prgrms2.java.bitta.jobpost.entity;

import com.prgrms2.java.bitta.apply.entity.Apply;
import com.prgrms2.java.bitta.media.entity.Media;
import com.prgrms2.java.bitta.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    private Member member;              // 게시글 작성자

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

    @Enumerated(EnumType.STRING)
    private ShootMethod shootMethod;   // 촬영 방법

    private LocalDate auditionDate;     // 오디션 일자

    private LocalDate startDate;   // 촬영 기간 시작일
    private LocalDate endDate;     // 촬영 기간 종료일

    @Transient
    private boolean isClosed;   // 게시글의 마감 여부

    public boolean isClosed() {
        return LocalDate.now().isAfter(this.endDate);
    }

    @OneToOne(mappedBy = "jobPost", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Media media;

    @OneToMany(mappedBy = "jobPost", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Apply> apply = new ArrayList<>();
}
