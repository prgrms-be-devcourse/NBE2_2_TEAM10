package com.prgrms2.java.bitta.user.entity;


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
@Table(name = "user")
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    private String location;

    private String profilePicture;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Feed> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostApplication> postApplications = new ArrayList<>();


    public void changeUsername(String username) {
        this.username = username;
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
        feed.setUser(this);  // 양방향 연관관계 설정
    }

    public void removeFeed(Feed feed) {
        this.feeds.remove(feed);
        feed.setUser(null);  // 연관관계 해제
    }

    public void addPostApplication(PostApplication postApplication) {
        this.postApplications.add(postApplication);
        postApplication.setUser(this);  // 양방향 연관관계 설정
    }

    public void removePostApplication(PostApplication postApplication) {
        this.postApplications.remove(postApplication);
        postApplication.setUser(null);  // 연관관계 해제
    }
}
