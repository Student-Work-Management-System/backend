package edu.guet.studentworkmanagementsystem.service.user.impl;

import cn.hutool.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.RolePermissionDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.UserRoleDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.LoginUserDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUserDTO;
import edu.guet.studentworkmanagementsystem.entity.po.user.*;
import edu.guet.studentworkmanagementsystem.entity.vo.authority.RolePermissionVO;
import edu.guet.studentworkmanagementsystem.entity.vo.user.UserDetailVO;
import edu.guet.studentworkmanagementsystem.entity.vo.user.LoginUserVO;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.authority.RolePermissionMapper;
import edu.guet.studentworkmanagementsystem.mapper.authority.UserRoleMapper;
import edu.guet.studentworkmanagementsystem.mapper.user.PermissionMapper;
import edu.guet.studentworkmanagementsystem.mapper.user.RoleMapper;
import edu.guet.studentworkmanagementsystem.mapper.user.UserMapper;
import edu.guet.studentworkmanagementsystem.securiy.SecurityUser;
import edu.guet.studentworkmanagementsystem.securiy.SystemAuthority;
import edu.guet.studentworkmanagementsystem.service.user.UserService;
import edu.guet.studentworkmanagementsystem.utils.JsonUtil;
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

import java.util.*;
import java.util.stream.Collectors;

import static edu.guet.studentworkmanagementsystem.entity.po.user.table.PermissionTableDef.PERMISSION;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.RolePermissionTableDef.ROLE_PERMISSION;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.RoleTableDef.ROLE;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.UserRoleTableDef.USER_ROLE;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Value("${jwt.key}")
    private String key;
    @Override
    public BaseResponse<LoginUserVO> login(LoginUserDTO loginUserDTO) {
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
        try {
            redisUtil.setValue(redisKey, JsonUtil.mapper.writeValueAsString(securityUser));
        } catch (JsonProcessingException jsonProcessingException) {
            ResponseUtil.failure(ServiceExceptionEnum.OPERATE_ERROR);
        }
        LoginUserVO loginUserVO = new LoginUserVO(securityUser.getUser(), (List<SystemAuthority>) securityUser.getAuthorities(), token);
        return ResponseUtil.success(loginUserVO);
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
                userRoles.add(new UserRole(uid, registerUserDTO.getRoles().getFirst()));
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
                if (roles.size() == 1)
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
    @Override
    public BaseResponse<UserDetailVO> getUserDetails(String username) {
        UserDetailVO userDetailVO = new UserDetailVO(mapper.getUserByUsername(username));
        String uid = userDetailVO.getUid();
        List<Role> userRole = userRoleMapper.getUserRole(uid);
        userDetailVO.setRoles(userRole);
        return ResponseUtil.success(userDetailVO);
    }
    @Transactional
    @Override
    public <T> BaseResponse<T> updateUserRole(UserRoleDTO userRoleDTO) {
        String uid = userRoleDTO.getUid();
        Set<String> roleSetFromDB = userRoleMapper.getUserRole(uid).stream()
                .map(Role::getRid)
                .collect(Collectors.toSet());
        Set<String> roleSetFromWeb = userRoleDTO.getRoles();
        List<String> delete = getDelete(roleSetFromWeb, roleSetFromDB).stream().toList();
        delete.forEach(item -> userRoleMapper.delete(new UserRole(uid, item)));
        List<String> insert = getInsert(roleSetFromWeb, roleSetFromDB).stream().toList();
        insert.forEach(item -> userRoleMapper.insert(new UserRole(uid, item)));
        return ResponseUtil.success();
    }
    @Transactional
    @Override
    public <T> BaseResponse<T> updateRolePermission(RolePermissionDTO rolePermissionDTO) {
        String rid = rolePermissionDTO.getRid();
        Set<String> permissionSetFromDB = rolePermissionMapper.getRolePermission(rid).stream()
                .map(Permission::getPermissionName)
                .collect(Collectors.toSet());
        Set<String> permissionSetFromWeb = rolePermissionDTO.getPermissions();
        List<String> delete = getDelete(permissionSetFromWeb, permissionSetFromDB).stream().toList();
        delete.forEach(item -> rolePermissionMapper.delete(new RolePermission(rid, item)));
        List<String> insert = getInsert(permissionSetFromWeb, permissionSetFromDB).stream().toList();
        insert.forEach(item -> rolePermissionMapper.insert(new RolePermission(rid, item)));
        return ResponseUtil.success();
    }
    @Transactional
    @Override
    public <T> BaseResponse<T> addRole(Role role) {
        int i = roleMapper.insert(role);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Transactional
    @Override
    public <T> BaseResponse<T> addPermission(Permission permission) {
        int i = permissionMapper.insert(permission);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Transactional
    @Override
    public <T> BaseResponse<T> deleteRole(String rid) {
        QueryWrapper rolePermissionWrapper = QueryWrapper.create().from(ROLE_PERMISSION).where(ROLE_PERMISSION.RID.eq(rid));
        QueryWrapper userRoleWrapper = QueryWrapper.create().from(USER_ROLE).where(USER_ROLE.RID.eq(rid));
        long rolePermissionNumber = rolePermissionMapper.selectCountByQuery(rolePermissionWrapper);
        long userRoleNumber = userRoleMapper.selectCountByQuery(userRoleWrapper);
        if (rolePermissionNumber != 0) {
            int i = rolePermissionMapper.deleteByQuery(rolePermissionWrapper);
            if (i != rolePermissionNumber)
                throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        }
        if (userRoleNumber != 0) {
            int i = userRoleMapper.deleteByQuery(userRoleWrapper);
            if (i != rolePermissionNumber)
                throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        }
        QueryWrapper roleWrapper = QueryWrapper.create().from(ROLE).where(ROLE.RID.eq(rid));
        int i = roleMapper.deleteByQuery(roleWrapper);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Transactional
    @Override
    public <T> BaseResponse<T> deletePermission(String pid) {
        QueryWrapper rolePermissionWrapper = QueryWrapper.create().from(ROLE_PERMISSION).where(ROLE_PERMISSION.PID.eq(pid));
        QueryWrapper permissionWrapper = QueryWrapper.create().from(PERMISSION).where(PERMISSION.PID.eq(pid));
        long rolePermissionNumber = rolePermissionMapper.selectCountByQuery(rolePermissionWrapper);
        if (rolePermissionNumber != 0) {
            int i = rolePermissionMapper.deleteByQuery(rolePermissionWrapper);
            if (i != rolePermissionNumber)
                throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        }
        int i = permissionMapper.deleteByQuery(permissionWrapper);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    public BaseResponse<List<RolePermissionVO>> getAllRole() {
        List<Role> roles = roleMapper.selectAll();
        ArrayList<RolePermissionVO> rolePermissionList = new ArrayList<>();
        roles.forEach(item -> {
            List<Permission> permission = rolePermissionMapper.getRolePermission(item.getRid());
            rolePermissionList.add(new RolePermissionVO(item, permission));
        });
        return ResponseUtil.success(rolePermissionList);
    }
    private Set<String> getInsert(Set<String> roleSetFromWeb, Set<String> roleSetFromDB) {
        return roleSetFromWeb.stream()
                .filter(element -> !roleSetFromDB.contains(element))
                .collect(Collectors.toSet());
    }
    private Set<String> getDelete(Set<String> roleSetFromWeb, Set<String> roleSetFromDB) {
        return roleSetFromDB.stream()
                .filter(element -> !roleSetFromWeb.contains(element))
                .collect(Collectors.toSet());
    }
}
