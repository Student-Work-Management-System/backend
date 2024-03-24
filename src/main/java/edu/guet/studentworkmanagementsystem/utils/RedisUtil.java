package edu.guet.studentworkmanagementsystem.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Value("${jwt.expire-time}")
    private Integer expireTime;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.DAYS);
    }
    public Object getValue(String key) {
       return redisTemplate.opsForValue().get(key);
    }
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
