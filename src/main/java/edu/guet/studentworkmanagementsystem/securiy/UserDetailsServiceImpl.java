package edu.guet.studentworkmanagementsystem.securiy;

import edu.guet.studentworkmanagementsystem.entity.po.user.Permission;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    @Qualifier("readThreadPool")
    private ThreadPoolTaskExecutor readThreadPool;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            CompletableFuture<User> userCompletableFuture = CompletableFuture
                    .supplyAsync(() -> userMapper.getUserByUsername(username), readThreadPool);
            CompletableFuture<SecurityUser> securityUserCompletableFuture = userCompletableFuture
                    .thenComposeAsync((user) -> CompletableFuture
                            .supplyAsync(() -> new SecurityUser(user, getUserPermission(user.getUid())), readThreadPool), readThreadPool);
            return securityUserCompletableFuture.get(3, TimeUnit.SECONDS);
        } catch (Exception exception) {
            Throwable cause = exception.getCause();
            switch (cause) {
                case ServiceException serviceException ->
                        throw serviceException;
                case TimeoutException ignored ->
                        throw new ServiceException(ServiceExceptionEnum.GET_RESOURCE_TIMEOUT);
                case InterruptedException ignored ->
                        throw new ServiceException(ServiceExceptionEnum.GET_RESOURCE_INTERRUPTED);
                default ->
                        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
            }
        }
    }
    private ArrayList<SystemAuthority> getUserPermission(String uid) {
        List<Permission> userPermission = userMapper.getUserPermission(uid);
        ArrayList<SystemAuthority> systemAuthorities = new ArrayList<>();
        for (Permission permission : userPermission) {
            SystemAuthority systemAuthority = new SystemAuthority(permission.getPermissionName(), permission.getPermissionDesc());
            systemAuthorities.add(systemAuthority);
        }
        return systemAuthorities;
    }
}
