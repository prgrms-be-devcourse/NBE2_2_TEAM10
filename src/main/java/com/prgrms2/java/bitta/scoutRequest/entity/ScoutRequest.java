package com.prgrms2.java.bitta.scoutRequest.entity;

import com.prgrms2.java.bitta.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "scout_request")
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ScoutRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scoutId;

    @OneToMany // 아직 설정 안됨
    private List<User> director;

    @OneToMany // 아직 설정 안됨
    private List<User> actor;

    private String message;

    @CreatedDate
    private LocalDateTime createdAt;

    private String Status;
    // accepted denied 수락여부임 기본값 설정 필요할듯.

}













