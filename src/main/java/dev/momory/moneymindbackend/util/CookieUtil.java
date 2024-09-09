package dev.momory.moneymindbackend.util;

import dev.momory.moneymindbackend.dto.TokenCategory;
import jakarta.servlet.http.Cookie;

/**
 * 쿠키 생성과 관련된 유틸리티 클래스
 */
public class CookieUtil {

    /**
     * 주어진 키(key)와 값(value)으로 새로운 쿠키를 생성하는 메서드.
     * 이 메서드는 HTTP 응답에 설정할 쿠키를 만들고, 기본적인 속성들을 설정합니다.
     * @param key 쿠키의 이름
     * @param value 쿠키의 값
     * @return 설정된 속성을 가진 새로운 쿠키 객체
     */
    public static Cookie createCookie(String key, String value) {
        // 주어진 키와 값으로 새로운 쿠키 객체 생성
        Cookie cookie = new Cookie(key, value);

        // 쿠키의 유효 기간을 설정(단위:초)
        // 현재 설정된 값은 24시간(24시간 * 60분 * 60초)
        cookie.setMaxAge(24 * 60 * 60);

        // 쿠키가 HTTPS 연결에서만 전송되도록 설정
         cookie.setSecure(false);

        // 쿠키의 유효 경로 설정
         cookie.setPath("/");

        // 쿠키를 HttpOnly 속성으로 설정
        // JavaScript에서 쿠키에 접근하지 못하도록 방지하여 보안을 강화하는 설정
        cookie.setHttpOnly(true);

        return cookie;
    }

    /**
     * 전달받은 쿠키 배열에서 특정 이름의 쿠키 값을 찾아 반환
     * @param cookies 쿠키 배열
     * @return 쿠키 배열에서 찾은 특정 이름의 쿠키 값 반환,
     *         해당 이름의 쿠키가 없거나 배열이 null일 경우 null 반환
     */
    public static String getCookieValue(Cookie[] cookies) {
        // 쿠키 배열이 null인 경우, 배열이 전달되지 않았거나 비어있는 경우에는 null을 반환
        if (cookies == null) {
            return null;
        }

        // 쿠키 배열을 순회하여 각 쿠키를 확인
        for (Cookie cookie : cookies) {
            // 쿠키이름이 refresh인지 체크
            if (TokenCategory.REFRESH.getValue().equals(cookie.getName())) {
                // 쿠키의 이름이 일치하면 해당 쿠키의 값을 반환합니다.
                return cookie.getValue();
            }
        }

        // 지정된 이름의 쿠키가 배열에 없으면 null을 반환합니다.
        return null;
    }
}
