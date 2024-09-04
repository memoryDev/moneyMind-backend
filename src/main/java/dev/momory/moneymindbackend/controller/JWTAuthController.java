package dev.momory.moneymindbackend.controller;

import dev.momory.moneymindbackend.dto.TokenCategory;
import dev.momory.moneymindbackend.service.JWTAuthService;
import dev.momory.moneymindbackend.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * JWT 인증 및 토큰 재발급을 담당하는 컨트롤러
 * 클라이언트로부터 요청을 받아 JWT 토큰을 검증하고, 필요한 경우 새로운 토큰을 발급합니다.
 */
@RestController
@RequiredArgsConstructor
public class JWTAuthController {

    private final JWTAuthService jwtAuthService;

    /**
     * 클라이언트로부터의 요청에서 리프레시 토큰을 추출하고,
     * 토큰을 검증하여 새로운 액세스 토큰과 리프레시 토큰을 발급합니다.
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @return HTTP 상태 코드와 함께 요청 처리 결과를 포함한 ResponseEntity
     */
    @PostMapping("/api/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        // 요청에서 리프레시 토큰을 쿠키에서 추출
        String refresh = CookieUtil.getCookieValue(request.getCookies());

        try {
            // JWTAuthService를 사용하여 리프레시 토큰 검증 및 새 토큰 발급
            String[] tokens = jwtAuthService.validateAndReissueTokens(refresh).split(":");

            // 응답 헤더에 새로운 액세스 토큰 설정
            response.setHeader(TokenCategory.ACCESS.getValue(), tokens[0]);

            // 응답에 새로운 리프레시 토큰을 쿠키로 추가
            response.addCookie(CookieUtil.createCookie(TokenCategory.REFRESH.getValue(), tokens[1]));

            // 응답 성공
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // 토큰 검증 또는 재발급 중 발생한 예외에 대해 BAD_REQUEST 응답 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
}
