package dev.momory.moneymindbackend.dto;

import dev.momory.moneymindbackend.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * CustomUserDetails 클래스는 Spring Security의 UserDetail 인터페이스를 구현하여
 * 사용자 정보 커스터마이징 하여 제공
 * 사용자 엔티티(User)를 기반으로 정보를 반환
 */
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetails implements UserDetails {

    private final User user;

    /**
     * 사용자 권한 정보 반환
     * @return 빈배열(현재 프로젝트에서는 권한 사용하지 않음)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * 사용자의 비밀번호를 반환합니다.
     * @return User 엔티티에 저장된 암호하된 비밀번호
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * 사용자 이름(실명)을 반환
     * @return User 엔티티에 저장된 사용자 이름
     */
    @Override
    public String getUsername() {
        return user.getUserid();
    }

    /**
     * 사용자 계정(로그인) 반환
     * @return User 엔티티에 저장된 사용자 계정
     */
    public String getUserid() {
        return user.getUserid();
    }

    /**
     * 사용자의 계정이 만료되지 않았는지 반환
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 사용자의 계정이 잠기지 않았는지를 반환
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 사용자의 자격 증명이 만료되지 않았는지를 반환
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 사용자의 계정이 활성화되어 있는지를 반환
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
