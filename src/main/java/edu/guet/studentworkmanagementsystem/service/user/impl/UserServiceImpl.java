package edu.guet.studentworkmanagementsystem.service.user.impl;

import cn.hutool.jwt.JWT;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.user.LoginUserDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUserDTO;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.entity.po.user.UserRole;
import edu.guet.studentworkmanagementsystem.entity.vo.user.UserVO;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.authority.UserRoleMapper;
import edu.guet.studentworkmanagementsystem.mapper.user.UserMapper;
import edu.guet.studentworkmanagementsystem.securiy.SecurityUser;
import edu.guet.studentworkmanagementsystem.securiy.SystemAuthority;
import edu.guet.studentworkmanagementsystem.service.user.UserService;
import edu.guet.studentworkmanagementsystem.utils.RedisUtil;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Value("${jwt.key}")
    private String key;
    @Override
    public BaseResponse<UserVO> login(LoginUserDTO loginUserDTO) {
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
        UserVO userVO = new UserVO(securityUser.getUser(), (List<SystemAuthority>) securityUser.getAuthorities(), token);
        return ResponseUtil.success(userVO);
    }
    @Transactional
    @Override
    public <T> BaseResponse<T> addUser(RegisterUserDTO registerUserDTO) {
        registerUserDTO.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        User user = new User(registerUserDTO);
        ArrayList<UserRole> userRoles = new ArrayList<>();
        int i = mapper.insert(user);
        if (i > 0) {
            List<String> roles = registerUserDTO.getRoles();
            String uid = user.getUid();
            if (roles.size() == 1)
                userRoles.add(new UserRole(user.getUid(), registerUserDTO.getRoles().getFirst()));
            else
                roles.forEach(item -> userRoles.add(new UserRole(uid, item)));
            int j = userRoleMapper.insertBatch(userRoles);
            if (j == roles.size())
                return ResponseUtil.success();
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        }
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Transactional
    @Override
    public <T> BaseResponse<T> addUsers(List<RegisterUserDTO> registerUserDTOList) {
        ArrayList<User> users = new ArrayList<>();
        ArrayList<UserRole> userRoles = new ArrayList<>();
        registerUserDTOList.forEach(item -> {
            item.setPassword(passwordEncoder.encode(item.getPassword()));
            users.add(new User(item));
        });
        int size = users.size();
        int i = mapper.insertBatch(users);
        if (i == size) {
            for(int j = 0; j < size; j++) {
                String uid = users.get(j).getUid();
                List<String> roles = registerUserDTOList.get(j).getRoles();
                if (registerUserDTOList.get(j).getRoles().size() == 1)
                    userRoles.add(new UserRole(uid, roles.getFirst()));
                else
                    roles.forEach(item -> userRoles.add(new UserRole(uid, item)));
            }
            int roleSize = userRoles.size();
            int j = userRoleMapper.insertBatch(userRoles);
            if (roleSize == j)
                return ResponseUtil.success();
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        }
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
}
