package dev.momory.moneymindbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그아웃시 전달받은 토큰
 */
@Getter
@AllArgsConstructor
public class LogoutRequest {
    private String access;
}
