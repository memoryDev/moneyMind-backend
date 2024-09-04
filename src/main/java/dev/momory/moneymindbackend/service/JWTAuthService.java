package dev.momory.moneymindbackend.service;

import dev.momory.moneymindbackend.dto.TokenCategory;
import dev.momory.moneymindbackend.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * JWT 토큰의 유효성을 검증하고, 만료된 경우 새로 발급하는 서비스 클래스
 * 이 클래슨느 JWTUtil을 사용하여 토큰의 유효성을 검사하고 새로운 액세스 및 리프레시 토큰을 생성합니다.
 */
@Service
@RequiredArgsConstructor
public class JWTAuthService {

    private final JWTUtil jwtUtil;

    /**
     * 리프레시 토큰을 검증하고, 유효할 경우 새로운 액세스 및 리프레시 토큰을 생성하여 반환합니다.
     *@param refresh 리프레시 토큰
     *@return 새로운 액세스 토큰과 리프레시 토큰을 콜론(:)으로 구분한 문자열
     *@throws IllegalArgumentException 리프레시 토큰이 null이거나 유효하지 않은 경우
     *@throws IllegalStateException 리프레시 토큰이 만료된 경우
     */
    public String validateAndReissueTokens(String refresh) {

        // refresh 토큰이 null인지 확인
        if (refresh == null) {
            throw new IllegalArgumentException("Refresh token is null");
        }

        try {
            //JWTUtil을 사용하여 리프레시 토큰이 만료되었는지 확인
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            // 리프레시 토큰이 만료된 경우 예외 발생
            throw new IllegalStateException("Refresh token expired");
        }

        // 토큰의 카테고리를 가져옴
        String category = jwtUtil.getCategory(refresh);

        // 카테고리가 "refresh"인지 확인
        if (!TokenCategory.REFRESH.getValue().equals(category)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        // 리프레시 토큰에서 사용자 ID 추출
        String userid = jwtUtil.getUserid(refresh);

        // 새로운 액세스 토큰과 리프레시 토큰을 생성
        String newAccess = jwtUtil.createJwt(TokenCategory.ACCESS, userid, 600000L);
        String newRefresh = jwtUtil.createJwt(TokenCategory.REFRESH, userid, 86400000L);

        // 새로운 액세스 토큰과 리프레시 토큰을 콜론으로 구분하여 반환
        return newAccess + ":" + newRefresh;
    }
}
