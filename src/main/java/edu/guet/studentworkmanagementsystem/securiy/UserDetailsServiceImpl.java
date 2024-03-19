package edu.guet.studentworkmanagementsystem.securiy;

import edu.guet.studentworkmanagementsystem.entity.po.user.Permission;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.authority.PermissionMapper;
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
    @Autowired
    private PermissionMapper permissionMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.getUserById(username);
        if (!Objects.isNull(user)) {
            return new SecurityUser(user, getUserPermission(user.getUid()));
        }
        throw new ServiceException(ServiceExceptionEnum.ACCOUNT_NOT_FOUND);
    }
    private ArrayList<SystemAuthority> getUserPermission(String uid) {
        List<Permission> userPermission = permissionMapper.getUserPermission(uid);
        ArrayList<SystemAuthority> systemAuthorities = new ArrayList<>();
        for (Permission permission : userPermission) {
            SystemAuthority systemAuthority = new SystemAuthority(permission.getPermissionName(), permission.getPermissionDescription());
            systemAuthorities.add(systemAuthority);
        }
        return systemAuthorities;
    }
}
