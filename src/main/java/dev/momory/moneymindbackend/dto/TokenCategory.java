package dev.momory.moneymindbackend.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * JWT 토큰 유형을 관리하는 enum 클래스
 */
@Getter
@RequiredArgsConstructor
public enum TokenCategory {
    /**
     * ACCESS : 엑세스 토큰을 나타내는 상수
     * REFRESH : 리프레시 토큰을 나타내는 상수
     */
    ACCESS("access"),
    REFRESH("refresh");

    private final String value;

}
