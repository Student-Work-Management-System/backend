package edu.guet.studentworkmanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
    private static final int CORE_POOL_SIZE = 10;
    private static final int MAXIMUM_POOL_SIZE = 20;
    private static final int QUEUE_CAPACITY = 50;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final String EMAIL_PREFIX = "emailThread-";
    private static final String READ_PREFIX = "readThread-";
    private static final String WRITE_PREFIX = "writeThread-";
    private static final ThreadPoolExecutor.CallerRunsPolicy HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();
    @Bean("emailThreadPool")
    public ThreadPoolTaskExecutor emailThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setRejectedExecutionHandler(HANDLER);
        executor.setThreadNamePrefix(EMAIL_PREFIX);
        executor.initialize();
        return executor;
    }
    @Bean("readThreadPool")
    public ThreadPoolTaskExecutor readThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAXIMUM_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setRejectedExecutionHandler(HANDLER);
        executor.setThreadNamePrefix(READ_PREFIX);
        executor.initialize();
        return executor;
    }
    @Bean("writeThreadPool")
    public ThreadPoolTaskExecutor writeThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAXIMUM_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setRejectedExecutionHandler(HANDLER);
        executor.setThreadNamePrefix(WRITE_PREFIX);
        executor.initialize();
        return executor;
    }
}
