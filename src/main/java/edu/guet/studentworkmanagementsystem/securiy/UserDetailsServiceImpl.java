package edu.guet.studentworkmanagementsystem.securiy;

import edu.guet.studentworkmanagementsystem.entity.po.user.Permission;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.mapper.user.UserMapper;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
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

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    @Qualifier("readThreadPool")
    private ThreadPoolTaskExecutor readThreadPool;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CompletableFuture<User> userCompletableFuture = CompletableFuture.supplyAsync(() -> userMapper.getUserByUsername(username), readThreadPool);
        CompletableFuture<SecurityUser> future = userCompletableFuture
                .thenComposeAsync((user) -> CompletableFuture
                        .supplyAsync(() -> new SecurityUser(user, getUserPermission(user.getUid())), readThreadPool), readThreadPool);
        return FutureExceptionExecute.fromFuture(future).execute();
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
