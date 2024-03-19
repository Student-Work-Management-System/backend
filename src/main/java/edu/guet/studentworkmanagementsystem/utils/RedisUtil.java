package edu.guet.studentworkmanagementsystem.utils;


import edu.guet.studentworkmanagementsystem.securiy.SecurityUser;
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
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.DAYS);
    }
    public SecurityUser getValue(String key) {
       return (SecurityUser) redisTemplate.opsForValue().get(key);
    }
}
