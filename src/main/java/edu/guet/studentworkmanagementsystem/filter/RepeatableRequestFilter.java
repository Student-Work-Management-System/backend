package edu.guet.studentworkmanagementsystem.filter;

import edu.guet.studentworkmanagementsystem.common.RepeatableReadRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class RepeatableRequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequestWrapper wrapper;
        if (servletRequest instanceof HttpServletRequest &&
                StringUtils.startsWithIgnoreCase(servletRequest.getContentType(), MediaType.APPLICATION_JSON_VALUE)) {
            wrapper = new RepeatableReadRequestWrapper((HttpServletRequest) servletRequest);
            filterChain.doFilter(wrapper, servletResponse);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
