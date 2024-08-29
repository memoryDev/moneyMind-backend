package dev.momory.moneymindbackend.config;

import dev.momory.moneymindbackend.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration     // 현재 클래스가 설정 클래스 임을 명시하여 Spring 빈에 등록 됨
@EnableWebSecurity // Spring Security를 활성화 하는 애너테이션
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    /**
     * 비밀번호 암호화를 위한 PasswordEncoder 빈 정의
     * BCryptPasswordEncoder 인스턴스를 생성하여 반환(암호화 알고리즘 Bcrypt 사용)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //BcryptPasswordEncoder 빈 정의
    }

    /**
     * HTTP 보안 설정을 정의하는 SecurityFilterChain 빈 정의
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화, REST API 사용할 경우 비활성화함
                .authorizeHttpRequests(authorize ->
                        authorize
                                // 인증 없이 접근 가능한 URL패턴을 정의(로그인 및 OAuth2 경로
                                .requestMatchers("/api/auth/**", "/oauth2/**").permitAll()
                                // 그 외 모든 요청 인증 필요
                                .anyRequest().authenticated()
                )
                // OAuth2 로그인 설정 시작
                .oauth2Login(oauth2 -> oauth2
                        // OAuth2 인증 요청의 기본 경로 설정
                        .authorizationEndpoint(authrization ->
                                authrization.baseUri("/oauth2/authorize")
                        )
                        // OAuth2 인증 후 리다이렉트 되는 경로 설정
                        .redirectionEndpoint(redirection ->
                                redirection.baseUri("/oauth2/callback/*"))
                        // 사용자 정의 OAuth2UserService 를 사용하여 상요자 정보를 처리
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(customOAuth2UserService)
                        )
                );

        // JWT 인증 필터를 UsernaemePasswordAuthenticationFilter 앞에 추가
//        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 설정된 SecurityFilterChain 객체를 반환
        return http.build();
    }

}
