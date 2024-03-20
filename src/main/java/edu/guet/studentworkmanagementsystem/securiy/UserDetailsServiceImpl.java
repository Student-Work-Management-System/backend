package edu.guet.studentworkmanagementsystem.securiy;

import edu.guet.studentworkmanagementsystem.entity.po.user.Permission;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.getUserByUsername(username);
        if (!Objects.isNull(user)) {
            return new SecurityUser(user, getUserPermission(user.getUid()));
        }
        throw new ServiceException(ServiceExceptionEnum.ACCOUNT_NOT_FOUND);
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
