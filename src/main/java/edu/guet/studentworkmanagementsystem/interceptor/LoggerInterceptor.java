package edu.guet.studentworkmanagementsystem.interceptor;

import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class LoggerInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);
    private static final int MAX_REQUESTS_PER_MINUTE = 100;
    private static final String RATE_LIMIT_KEY_PREFIX = "rate_limit: ";
    private final RedisTemplate<String, Object> redisTemplate;
    public LoggerInterceptor(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        rateLimit(request, response);
        log(request);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
    private void rateLimit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String remoteAddr = request.getRemoteAddr();
        String key = RATE_LIMIT_KEY_PREFIX + remoteAddr;
        Long count = redisTemplate.opsForValue().increment(key, 1);
        if (count != null && count == 1)
            redisTemplate.expire(key, 1, TimeUnit.MINUTES);
        if (count != null && count > MAX_REQUESTS_PER_MINUTE)
            ResponseUtil.failure(response, ServiceExceptionEnum.TOO_MANY_REQUEST);
    }
    private void log(HttpServletRequest request) throws IOException {
        String method = request.getMethod();
        String servletPath = request.getServletPath();
        String remoteAddr = request.getRemoteAddr();
        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuffer buffer = new StringBuffer();
        if (!parameterMap.isEmpty()) {
            parameterMap.forEach((key, value) ->{
                if (!Objects.isNull(key) && !Objects.isNull(value)) {
                    buffer.append("key: ").append(key).append(",value: ").append(Arrays.toString(value)).append(" ");
                }
            });
        }
        if (request.getInputStream().readAllBytes().length > 0) {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null)
                    buffer.append(line.trim());
            } catch (IOError e) {
                logger.error("出现错误: {}", e.toString());
            }
        }
        logger.info("接收到{}请求:{}, 来源:{}", method, servletPath, remoteAddr);
        if (StringUtils.hasLength(buffer.toString()))
            logger.info("参数: {}", buffer);
    }
}
