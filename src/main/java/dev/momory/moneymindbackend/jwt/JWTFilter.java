package dev.momory.moneymindbackend.jwt;

import dev.momory.moneymindbackend.dto.CustomUserDetails;
import dev.momory.moneymindbackend.entity.AuthProvider;
import dev.momory.moneymindbackend.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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

        // 요청 헤더에서 Authorization 헤더를 추출
        String authorization = request.getHeader("Authorization");
        log.info("JWTFilter.Authorization = {}", authorization);

        // Authorization 헤더가 없거나, "Bearer "로 시작하지 않을경우 종료
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.info("JWTFilter.token null");
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer 부분 제거후 토큰 추출
        String token = authorization.replace("Bearer ", "");

        // 토큰 만료 여부 추출
        Boolean tokenExpired = jwtUtil.isExpired(token);
        log.info("JWTFilter.tokenExpired = {}", tokenExpired);

        // 토큰이 만료 되었을경우 종료
        if (tokenExpired) {
            log.info("JWTFilter.tokenExpired null");
            filterChain.doFilter(request, response);
            return;
        }

        // JWT 토큰에서 [userid, username, email, authProvider]정보 추출
        String userid = jwtUtil.getUserid(token);
        String username = jwtUtil.getUsername(token);
        String email = jwtUtil.getUsername(token);
        AuthProvider authProvider = jwtUtil.getAuthProvider(token);

        // User 엔티티 객체 생성후 사용자 정보 설정
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
