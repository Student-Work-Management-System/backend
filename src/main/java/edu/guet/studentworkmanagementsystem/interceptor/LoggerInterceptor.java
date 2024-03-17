package edu.guet.studentworkmanagementsystem.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Component
public class LoggerInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
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
        if (StringUtils.hasLength(buffer.toString()))
            logger.info("参数: {}", buffer);
        logger.info("接收到" + method +"请求:" + servletPath + ", 来源:" + remoteAddr);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
