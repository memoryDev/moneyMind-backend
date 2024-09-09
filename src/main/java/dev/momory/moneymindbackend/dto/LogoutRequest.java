package dev.momory.moneymindbackend.dto;

import lombok.Getter;

/**
 * 로그아웃시 전달받은 토큰
 */
@Getter
public class LogoutRequest {
    private String access;
}
