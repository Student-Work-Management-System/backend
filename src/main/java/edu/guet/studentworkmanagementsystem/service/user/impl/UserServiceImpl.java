package edu.guet.studentworkmanagementsystem.service.user.impl;

import cn.hutool.jwt.JWT;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.entity.dto.user.LoginUserDTO;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.entity.vo.user.UserVO;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.user.UserMapper;
import edu.guet.studentworkmanagementsystem.securiy.SecurityUser;
import edu.guet.studentworkmanagementsystem.securiy.SystemAuthority;
import edu.guet.studentworkmanagementsystem.service.user.UserService;
import edu.guet.studentworkmanagementsystem.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisUtil redisUtil;
    @Value("${jwt.key}")
    private String key;
    @Override
    public UserVO login(LoginUserDTO loginUserDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUserDTO.getUsername(), loginUserDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate))
            throw new ServiceException(ServiceExceptionEnum.ACCOUNT_NOT_FOUND);
        SecurityUser securityUser = (SecurityUser) authenticate.getPrincipal();
        String redisKey = "uid:" + securityUser.getUser().getUid();
        String token = JWT.create()
                .setPayload("uid", redisKey)
                .setKey(key.getBytes())
                .sign();
        redisUtil.setValue(redisKey, securityUser);
        return new UserVO(securityUser.getUser(), (List<SystemAuthority>) securityUser.getAuthorities(), token);
    }
}
