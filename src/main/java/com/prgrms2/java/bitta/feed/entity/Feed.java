package com.prgrms2.java.bitta.feed.entity;

import com.prgrms2.java.bitta.user.entity.User;
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
public class Feed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long feedId;

    private String title;

    private String content;

    @ManyToOne(cascade = CascadeType.ALL) //User 삭제시 Feed 또한 같이 삭제되게
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
}
