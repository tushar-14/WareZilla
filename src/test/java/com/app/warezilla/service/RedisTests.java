package com.app.warezilla.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void redisConnectionTest() {
        String testKey = "testKey";
        String testValue = "testValue";

        // Set value in Redis
        redisTemplate.opsForValue().set(testKey, testValue);

        // Retrieve value from Redis
        String retrievedValue = (String) redisTemplate.opsForValue().get(testKey);

        // Assert the value is as expected
        assert testValue.equals(retrievedValue);

        String test = (String) redisTemplate.opsForValue().get(testValue);
        // Clean up
        redisTemplate.delete(testKey);
        redisTemplate.delete(testValue);
    }
}
