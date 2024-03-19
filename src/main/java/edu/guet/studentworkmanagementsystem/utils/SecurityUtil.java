package edu.guet.studentworkmanagementsystem.utils;

import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.securiy.SecurityUser;
import edu.guet.studentworkmanagementsystem.securiy.SystemAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;


final public class SecurityUtil {
    public static Authentication getInstance() {
        Authentication authentication;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            return authentication;
        } catch (Exception exception) {
            throw new ServiceException(ServiceExceptionEnum.UN_LOGIN);
        }
    }
    /**
     * 获取认证完成存储的主体标识, 这里为用户id
     * @return 认证主体标识
     * @see edu.guet.studentworkmanagementsystem.filter.AuthenticationFilter
     * 第55、56行传参
     */
    public static String getUserPrincipal() {
        Authentication instance = getInstance();
        return (String) instance.getPrincipal();
    }
    /**
     * 获取token认证完成后存储的详细信息,在本系统中为SecurityUser
     * @return 认证验证详细信息
     * @see edu.guet.studentworkmanagementsystem.filter.AuthenticationFilter
     * 第55、56行传参
     */
    public static SecurityUser getUserCredentials() {
        Authentication instance = getInstance();
        return (SecurityUser) instance.getCredentials();
    }
    /**
     * 获取用户所有的权限
     * @return 认证验证详细信息
     * @see edu.guet.studentworkmanagementsystem.filter.AuthenticationFilter
     * 第55、56行传参
     */
    public static List<SystemAuthority> getUserAuthorities() {
        Authentication instance = getInstance();
        return (List<SystemAuthority>) instance.getAuthorities();
    }
}
