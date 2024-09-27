package com.prgrms2.java.bitta.member.entity;


import com.prgrms2.java.bitta.application.entity.PostApplication;
import com.prgrms2.java.bitta.feed.entity.Feed;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "member")
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String memberName;

    @Column(unique = true)
    private String email;

    private String password;

    private String location;

    private String profilePicture;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Feed> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostApplication> postApplications = new ArrayList<>();


    public void changeMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeLocation(String location) {
        this.location = location;
    }

    public void changeProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void changeRole(Role role) {
        this.role = role;
    }

    public void addFeed(Feed feed) {
        this.feeds.add(feed);
        feed.setMember(this);  // 양방향 연관관계 설정
    }

    public void removeFeed(Feed feed) {
        this.feeds.remove(feed);
        feed.setMember(null);  // 연관관계 해제
    }

    public void addPostApplication(PostApplication postApplication) {
        this.postApplications.add(postApplication);
        postApplication.setMember(this);  // 양방향 연관관계 설정
    }

    public void removePostApplication(PostApplication postApplication) {
        this.postApplications.remove(postApplication);
        postApplication.setMember(null);  // 연관관계 해제
    }
}
