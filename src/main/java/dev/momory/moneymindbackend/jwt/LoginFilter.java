package dev.momory.moneymindbackend.jwt;

import dev.momory.moneymindbackend.dto.CustomUserDetails;
import dev.momory.moneymindbackend.dto.TokenCategory;
import dev.momory.moneymindbackend.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

/**
 * LoginFilter 클래스는 사용자의 로그인 요청을 처리하고,
 * 인증이 성공적으로 완료된 후 JWT 토큰을 생성하여 응답(header)에 추가함
 */
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        // 로그인 URL 변경
        setFilterProcessesUrl("/api/login");
        // 로그인 계정 파라미터 변경
        setUsernameParameter("userid");
    }

    /**
     * 사용자의 로그인 요청을 처리합니다.
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @return Authentication 객체
     * @throws AuthenticationException 인증 실패 시 예외
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // 요청에서 사용자아이디, 비밀번호 추출
        String userid = obtainUsername(request);
        String password = obtainPassword(request);
        log.info("userid = {}", userid);

        // 사용자 이름과 비밀번호를 포함한 인증 토큰 생성
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userid, password);

        // AuthenticationManager를 통해 인증 시도
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    /**
     * 인증이 성공적으로 완료된 후 호출됨
     * JWT 토큰을 생성하여 응답 헤더에 추가합니다.
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param chain 필터 체인
     * @param authentication 인증 정보
     * @throws IOException 입출력 예외
     * @throws ServletException 서블릿 예외
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        log.info("successful authentication");

        // 인증된 사용자 정보를 CustomUserDetails에서 가져옵니다.
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        // 사용자 아이디 추출
        String userid = customUserDetails.getUserid();

        // JWT 토큰 생성
        String access = jwtUtil.createJwt(TokenCategory.ACCESS, userid, 600000L);
        String refresh = jwtUtil.createJwt(TokenCategory.REFRESH, userid, 86400000L);

        // 응답 설정
        response.setHeader(TokenCategory.ACCESS.getValue(), access);
        response.addCookie(CookieUtil.createCookie(TokenCategory.REFRESH.getValue(), refresh));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * 인증이 실패한 경우 호출
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param failed 인증 실패 예외
     * @throws IOException 입출력 예외
     * @throws ServletException 서블릿 예외
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("LoginFilter.unsuccessfulAuthentication");

        // 응답 코드 401 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
