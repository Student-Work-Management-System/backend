package edu.guet.studentworkmanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final int MAXIMUM_POOL_SIZE = CORE_POOL_SIZE + 1;
    private static final int QUEUE_CAPACITY = 1000;
    private static final int KEEP_ALIVE_TIME = 10;
    private static final String PREFIX = "emailThread-";
    private static final ThreadPoolExecutor.CallerRunsPolicy HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();
    @Bean("emailThreadPool")
    public ThreadPoolTaskExecutor emailThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAXIMUM_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setRejectedExecutionHandler(HANDLER);
        executor.setThreadNamePrefix(PREFIX);
        executor.initialize();
        return executor;
    }
}
