package dev.momory.moneymindbackend.jwt;

import dev.momory.moneymindbackend.dto.CustomUserDetails;
import dev.momory.moneymindbackend.dto.TokenCategory;
import dev.momory.moneymindbackend.entity.AuthProvider;
import dev.momory.moneymindbackend.entity.User;
import dev.momory.moneymindbackend.exception.CustomException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * JWTFilter 클래스는 HTTP요청에서 JWT 토큰을 검증하고,
 * 인증 정보를 SecurityContext에 설정하는 필터 클래스 입니다.
 * Spring Security의 OncePerRequestFilter를 확장하여 요청당 한번만 실행됨
 */
@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    /**
     * HTTP 요청을 필터링하고 JWT 토큰을 검증합니다.
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤더에서 "access" 토큰 추출
        String accessToken = request.getHeader(TokenCategory.ACCESS.getValue());

        // access 토큰이 없을 경우, 다음 필터로 이동하여 요청을 처리
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // JWT 토큰 만료 여부 확인
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            log.warn("JWTFilter.token 만료");
            // 토큰이 만료된 경우, 응답에 만료 메세지 작성
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            // HTTP 응답 상태를 401로 설정(Unauthorized)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // JWT 토큰에서 "category" 값을 추출
        String category = jwtUtil.getCategory(accessToken);

        // "category" 값이 "access"인지 체크
        if (!TokenCategory.ACCESS.getValue().equals(category)) {
            PrintWriter writer = response.getWriter();
            writer.print("access denied");

            // HTTP 응답 상태를 401로 설정(Unauthorized)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰에서 사용자 정보를 추출
        String userid = jwtUtil.getUserid(accessToken);
        String username = jwtUtil.getUsername(accessToken);
        String email = jwtUtil.getUsername(accessToken);
        AuthProvider authProvider = jwtUtil.getAuthProvider(accessToken);

        // 사용자 정보를 담은 User 객체 생성
        User user = new User();
        user.setUserid(userid);
        user.setUsername(username);
        user.setEmail(email);
        user.setAuthProvider(authProvider);

        // CustomUserDetails 객체를 생성후 사용자 세부 설정
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // UsernamePasswordAuthenticationToken 객체를 생성하여 인증 정보 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, null);

        // SecurityContext 인증 정보를 설정
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 필터 체인을 계속 진행
        filterChain.doFilter(request, response);
    }
}
