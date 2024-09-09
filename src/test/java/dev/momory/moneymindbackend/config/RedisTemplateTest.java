package dev.momory.moneymindbackend.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
public class RedisTemplateTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void redisTemplateString() {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();


        String key = "name";
        valueOperations.set(key, "nameTest");

        String value = valueOperations.get(key);
        Assertions.assertEquals(value, "nameTest");
    }

    @Test
    void redisTemplateDelete() {
        Boolean isDelete = redisTemplate.delete("name");

        Assertions.assertTrue(isDelete);
    }
}
