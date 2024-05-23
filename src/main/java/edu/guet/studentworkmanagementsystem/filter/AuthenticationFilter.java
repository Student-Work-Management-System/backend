package edu.guet.studentworkmanagementsystem.filter;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.securiy.SecurityUser;
import edu.guet.studentworkmanagementsystem.utils.JsonUtil;
import edu.guet.studentworkmanagementsystem.utils.RedisUtil;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
final public class AuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private RedisUtil redisUtil;
    @Value("${jwt.key}")
    private String keyStr;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasLength(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String redisKey = getRedisKeyFromToken(token, response);
        if (redisKey == null) return;
        String json = getJsonFromRedis(redisKey, response);
        if (json == null) {
            respondWithFailure(response, ServiceExceptionEnum.UN_LOGIN);
            return;
        }
        SecurityUser securityUser = parseJsonToSecurityUser(json, response);
        if (securityUser == null) {
            respondWithFailure(response, ServiceExceptionEnum.UN_LOGIN);
            return;
        }
        setAuthentication(securityUser);
        filterChain.doFilter(request, response);
    }

    private String getRedisKeyFromToken(String token, HttpServletResponse response) throws IOException {
        try {
            JWT jwt = JWT.of(token);
            boolean verify = jwt.setKey(keyStr.getBytes()).verify();
            if (!verify) {
                respondWithFailure(response, ServiceExceptionEnum.TOKEN_ERROR);
                return null;
            }
            return jwt.getPayload("uid").toString();
        } catch (JWTException | IOException jwtException) {
            respondWithFailure(response, ServiceExceptionEnum.TOKEN_ERROR);
            return null;
        }
    }

    private String getJsonFromRedis(String redisKey, HttpServletResponse response) throws IOException {
        try {
            return (String) redisUtil.getValue(redisKey);
        } catch (NullPointerException e) {
            log.error("空指针错误: {}", e.getMessage());
            respondWithFailure(response, ServiceExceptionEnum.UN_LOGIN);
            return null;
        }
    }

    private SecurityUser parseJsonToSecurityUser(String json, HttpServletResponse response) throws IOException {
        try {
            return JsonUtil.mapper.readValue(json, SecurityUser.class);
        } catch (Exception e) {
            log.error("出现异常(可能为json解析异常): {}", e.getMessage());
            respondWithFailure(response, ServiceExceptionEnum.UN_LOGIN);
            return null;
        }
    }

    private void setAuthentication(SecurityUser securityUser) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                securityUser.getUser().getUid(), securityUser, securityUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private void respondWithFailure(HttpServletResponse response, ServiceExceptionEnum exceptionEnum) throws IOException {
        ResponseUtil.failure(response, exceptionEnum);
    }
}
