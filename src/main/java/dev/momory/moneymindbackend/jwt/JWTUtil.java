package dev.momory.moneymindbackend.jwt;

import dev.momory.moneymindbackend.dto.TokenCategory;
import dev.momory.moneymindbackend.entity.AuthProvider;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * JWTUtil 클래스는 JWT 토큰을 생성, 파싱 및 검증하는 데 사용되는 유틸리티 클래스
 */
@Component
public class JWTUtil {

    // 대칭 키 암호화를 위한 SecretKey 객체, JWT 서명 및 검증에 사용됨
    private SecretKey secretKey;

    public JWTUtil(@Value("${jwt.secretKey}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    /**
     * JWT 토큰에서 사용자아이디(계정)을 추출
     * @param token 사용자 인증에 사용되는 JWT토큰
     * @return 토큰에서 추출한 사용자 아이디
     */
    public String getUserid(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userid", String.class);
    }

    /**
     * JWT 토큰에서 사용자이름(실명)을 추출
     * @param token 사용자 인증에 사용되는 JWT토큰
     * @return 토큰에서 추출한 사용자 이름(실명)
     */
    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    /**
     * JWT 토큰에서 로그인타입을 추출
     * @param token 사용자 인증에 사용되는 JWT토큰
     * @return 토큰에서 추출한 로그인타입
     */
    public AuthProvider getAuthProvider(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("authProvider", AuthProvider.class);
    }

    /**
     * JWT 토큰에서 이메일을 추출
     * @param token 사용자 인증에 사용되는 JWT토큰
     * @return 토큰에서 추출한 이메일
     */
    public String getEmail(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }

    /**
     * JWT 토큰의 만료 여부를 확인
     * @param token 사용자 인증에 사용되는 JWT토큰
     * @return 토큰이 만료되었으면 true, 그렇지 않으면 false를 반환
     */
    public Boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    /**
     * JWT 토큰에서 유형 구분하여 추출
     * @param token 사용자 인증에 사용되는 JWT토큰
     * @return 토큰유형 추출
     */
    public String getCategory(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("category", String.class);
    }

    /**
     * 새로운 JWT 토큰을 생성
     * @param tokenCategory 토큰 카테고리(refresh/access)
     * @param userid 사용자 아이디
     * @param expiredMs 토큰의 만료 시간
     * @return 생성된 JWT 토큰 문자열
     */
    public String createJwt(TokenCategory tokenCategory, String userid, Long expiredMs) {
        return Jwts.builder()
                .claim("category", tokenCategory.getValue())
                .claim("userid", userid)
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
