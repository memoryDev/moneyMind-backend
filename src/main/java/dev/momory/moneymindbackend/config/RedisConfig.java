package dev.momory.moneymindbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    // redis 호스트 주소
    @Value("${spring.data.redis.host}")
    private String host;
    // redis port 번호
    @Value("${spring.data.redis.port}")
    private int port;

    /**
     * RedisConnectionFactory 빈을 생성합니다.
     * RedisConnectionFactory는 Redis  서버와의 연결을 관리하는 인터페이스입니다.
     * 이 메서드에서는 LettuceConnectionFactory를 사용하여 Redis 서버에 연결합니다.
     * @return LettuceConnectionFactory를 인스턴스
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // LettuceConnectionFactory는 Redis 서버의 호스트의 호스트와 포트를 설정하여 연결을 관리함
        return new LettuceConnectionFactory(host, port);
    }

    /**
     * RedisTemplate 빈 생성
     * RedisTemplate은 Redis 서버와의 데이터 조작을 위한 주요 인터페이스
     * 템플릿을 사용하여 Redis 서버에 데이터를 저장, 조회, 삭제할 수 있습니다.
     * @return RedisTemplate 인스턴스
     */
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        // RedisTemplate 객체 생성
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();

        // RedisTemplate에 RedisConnectionFactory를 설정하여 Redis 서버와의 연결을 설정
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // RedisTemplate 객체를 반환하여 빈으로 등록
        return redisTemplate;
    }
}
