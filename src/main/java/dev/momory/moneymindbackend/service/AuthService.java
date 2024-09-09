package dev.momory.moneymindbackend.service;

import dev.momory.moneymindbackend.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final RedisUtil redisUtil;
    private final RedisTemplate<String, String> redisTemplate;

    public void logout(String token, RedirectAttributes redirectAttributes) {
        // token 블랙리스트에 추가
        // 1시간동안 블랙리스트에 유지
        redisTemplate.opsForValue().set("blacklist:" + token, "true", Duration.ofHours(1));

        // 쿠키에서 Refresh Token 삭제
        redirectAttributes.addFlashAttribute("logoutSuccess", true);
    }
}
