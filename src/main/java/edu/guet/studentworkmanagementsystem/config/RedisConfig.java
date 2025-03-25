package edu.guet.studentworkmanagementsystem.config;

import edu.guet.studentworkmanagementsystem.utils.JsonUtil;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.database}")
    private int database;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.password}")
    private String password;
    @Value("${spring.data.redis.timeout}")
    private long timeout;
    @Value("${spring.data.redis.lettuce.shutdown-timeout}")
    private long shutDownTimeout;
    @Value("${spring.data.redis.lettuce.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.data.redis.lettuce.pool.min-idle}")
    private int minIdle;
    @Value("${spring.data.redis.lettuce.pool.max-active}")
    private int maxActive;
    @Value("${spring.data.redis.lettuce.pool.max-wait}")
    private long maxWait;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        GenericObjectPoolConfig<Object> genericObjectPoolConfig  = new GenericObjectPoolConfig<>();
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMaxWait(Duration.ofSeconds(maxWait));
        genericObjectPoolConfig.setEvictorShutdownTimeout(Duration.ofSeconds(timeout));
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setDatabase(database);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(password);
        LettucePoolingClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(timeout))
                .shutdownTimeout(Duration.ofSeconds(shutDownTimeout))
                .poolConfig(genericObjectPoolConfig)
                .build();
        return new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(JsonUtil.getMapper(), Object.class);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setConnectionFactory(factory);
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jsonRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(jsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

}
