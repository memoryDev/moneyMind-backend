package dev.momory.moneymindbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
public class SocialAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("소셜 계정 고유 ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    @Comment("사용자 고유 ID")
    private User user;

    @Comment("소셜 로그인 제공자")
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Comment("소셜 로그인 사용자 ID")
    private String providerUserId;

    @Comment("소셜 로그인 액세스 토큰")
    private String accessToken;

    @Comment("소셜 로그인 리프레시 토큰")
    private String refreshToken;
}
