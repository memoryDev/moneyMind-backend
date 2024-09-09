package dev.momory.moneymindbackend.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisUtil {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 키와 값을 Redis에 저장
     * @param key Redis에 저장할 키
     * @param value Redis에 저장할 값
     */
    public void redisSave(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 주어진 키에 해당하는 데이터를 Redis에서 삭제
     * @param key Redis에 저장할 키
     */
    public void redisDelete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 주어진 키에 해당하는 데이터를 Redis에서 조회합니다.
     * @param key Redis에 저장할 키
     * @return 조회된 값(없을 경우 null 반환)
     */
    public String getRedis(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 주어진 키가 Redis에 존재하는지 확인
     * @param key 존재 여부를 확인할 키
     * @return 키가 존재하면 true, 존재하지 않으면 false
     */
    public Boolean isRedis(String key) {
        return redisTemplate.hasKey(key);
    }
}
