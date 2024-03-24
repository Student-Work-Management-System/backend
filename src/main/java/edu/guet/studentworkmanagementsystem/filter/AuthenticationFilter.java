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
import java.util.Objects;

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
        String redisKey = null;
        try {
            JWT jwt = JWT.of(token);
            boolean verify = jwt.setKey(keyStr.getBytes()).verify();
            if (verify)
                redisKey = jwt.getPayload("uid").toString();
            else
                ResponseUtil.failure(response, ServiceExceptionEnum.TOKEN_ERROR);
        } catch (JWTException jwtException) {
            ResponseUtil.failure(response, ServiceExceptionEnum.TOKEN_ERROR);
            return;
        }
        String json = null;
        try {
            json = (String) redisUtil.getValue(redisKey);
        } catch (NullPointerException nullPointerException) {
            ResponseUtil.failure(response, ServiceExceptionEnum.UN_LOGIN);
            return;
        }
        SecurityUser securityUser = null;
        try {
            securityUser = JsonUtil.mapper.readValue(json, SecurityUser.class);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            ResponseUtil.failure(response, ServiceExceptionEnum.UN_LOGIN);
            return;
        }
        if (Objects.isNull(securityUser)) {
            ResponseUtil.failure(response, ServiceExceptionEnum.UN_LOGIN);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(securityUser.getUser().getUid(), securityUser, securityUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
