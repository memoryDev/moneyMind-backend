package dev.momory.moneymindbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("사용자 고유 ID")
    private Long id;

    @Comment("사용자 id")
    private String userid;

    @Column(nullable = false)
    @Comment("사용자 이름")
    private String username;

//    @Column(nullable = false)
    @Comment("이메일")
    private String email;

//    @Column(nullable = false)
    @Comment("비밀번호")
    private String password;

    @Comment("프로필 사진 URL")
    private String profilePicture;

    @Column(nullable = false)
    @Comment("계정 생성 날짜")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    @Comment("계정 수정 날짜")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Comment("로그인타입")
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Comment("소셜 로그인 사용자 ID")
    @OneToMany(mappedBy = "user")
    private List<SocialAccount> socialAccount;
}
