package edu.guet.studentworkmanagementsystem.service.user.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.RoleDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.RolePermissionDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.UserRoleDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.LoginUserDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUserDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUserDTOList;
import edu.guet.studentworkmanagementsystem.entity.dto.user.UpdateUserDTO;
import edu.guet.studentworkmanagementsystem.entity.po.user.*;
import edu.guet.studentworkmanagementsystem.entity.vo.authority.PermissionTreeVO;
import edu.guet.studentworkmanagementsystem.entity.vo.authority.RolePermissionVO;
import edu.guet.studentworkmanagementsystem.entity.vo.user.LoginUserVO;
import edu.guet.studentworkmanagementsystem.entity.vo.user.UserDetailVO;
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
import edu.guet.studentworkmanagementsystem.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static edu.guet.studentworkmanagementsystem.entity.po.user.table.PermissionTableDef.PERMISSION;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.RolePermissionTableDef.ROLE_PERMISSION;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.RoleTableDef.ROLE;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.UserRoleTableDef.USER_ROLE;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.UserTableDef.USER;

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
        String token = createToken(redisKey, key);
        try {
            redisUtil.setValue(redisKey, JsonUtil.mapper.writeValueAsString(securityUser));
        } catch (JsonProcessingException jsonProcessingException) {
            ResponseUtil.failure(ServiceExceptionEnum.OPERATE_ERROR);
        }
        LoginUserVO loginUserVO = new LoginUserVO(securityUser.getUser(), (List<SystemAuthority>) securityUser.getAuthorities(), token);
        return ResponseUtil.success(loginUserVO);
    }
    private boolean RoleNotNullOrEmpty(List<String> roles) {
        return !Objects.isNull(roles) && !roles.isEmpty();
    }
    private void addUserRole(List<String> roles, String uid) {
        ArrayList<UserRole> userRoles = new ArrayList<>();
        if (roles.size() == 1)
            userRoles.add(new UserRole(uid, roles.getFirst()));
        else
            roles.forEach(item -> userRoles.add(new UserRole(uid, item)));
        int i = userRoleMapper.insertBatch(userRoles);
        if (i == roles.size())
            return;
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Transactional
    @Override
    public <T> BaseResponse<T> addUser(RegisterUserDTO registerUserDTO) {
        registerUserDTO.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        User user = new User(registerUserDTO);
        int i = mapper.insert(user);
        if (i > 0) {
            List<String> roles = registerUserDTO.getRoles();
            String uid = user.getUid();
            if (RoleNotNullOrEmpty(roles))
                addUserRole(roles, uid);
            return ResponseUtil.success();
        }
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Transactional
    @Override
    public <T> BaseResponse<T> addUsers(RegisterUserDTOList registerUserDTOS) {
        List<RegisterUserDTO> registerUserDTOList = registerUserDTOS.getRegisterUserDTOList();
        ArrayList<User> users = new ArrayList<>();
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
                if (RoleNotNullOrEmpty(roles))
                    addUserRole(roles, uid);
            }
            return ResponseUtil.success();
        }
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    public BaseResponse<UserDetailVO> getUserDetails(String username) {
        UserDetailVO userDetailVO = new UserDetailVO(mapper.getUserByUsername(username));
        String uid = userDetailVO.getUid();
        List<Role> userRole = userRoleMapper.getUserRole(uid);
        if (!Objects.isNull(userRole))
            userDetailVO.setRoles(userRole);
        return ResponseUtil.success(userDetailVO);
    }
    private <T> boolean listHandler(List<T> list) {
        return Objects.isNull(list) || list.isEmpty() || (list.size() == 1 && Objects.isNull(list.getFirst()));
    }
    @Transactional
    @Override
    public <T> BaseResponse<T> updateUserRole(UserRoleDTO userRoleDTO) {
        String uid = userRoleDTO.getUid();
        List<Role> roles = userRoleMapper.getUserRole(uid);
        if (listHandler(roles)) {
            addUserRole(userRoleDTO.getRoles().stream().toList(), uid);
        } else {
            Set<String> roleSetFromDB = roles.stream()
                    .map(Role::getRid)
                    .filter(StringUtils::hasLength)
                    .collect(Collectors.toSet());
            Set<String> roleSetFromWeb = userRoleDTO.getRoles();
            List<String> delete = getDelete(roleSetFromWeb, roleSetFromDB).stream().toList();
            delete.forEach(item -> userRoleMapper.delete(new UserRole(uid, item)));
            List<String> insert = getInsert(roleSetFromWeb, roleSetFromDB).stream().toList();
            insert.forEach(item -> userRoleMapper.insert(new UserRole(uid, item)));
        }
        userRoleUpdateHandler(uid);
        return ResponseUtil.success();
    }
    private void addRolePermission(List<String> permissions, String rid) {
        ArrayList<RolePermission> rolePermission = new ArrayList<>();
        if (permissions.size() == 1)
            rolePermission.add(new RolePermission(rid, permissions.getFirst()));
        else
            permissions.forEach(item -> rolePermission.add(new RolePermission(rid, item)));
        int i = rolePermissionMapper.insertBatch(rolePermission);
        if (i == permissions.size())
            return;
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Transactional
    @Override
    public <T> BaseResponse<T> updateRolePermission(RolePermissionDTO rolePermissionDTO) {
        String rid = rolePermissionDTO.getRid();
        List<Permission> permissions = rolePermissionMapper.getRolePermission(rid);
        if (listHandler(permissions)) {
            addRolePermission(rolePermissionDTO.getPermissions().stream().toList(), rid);
        } else {
            Set<String> permissionSetFromDB = permissions.stream()
                    .map(Permission::getPid)
                    .filter(StringUtils::hasLength)
                    .collect(Collectors.toSet());
            Set<String> permissionSetFromWeb = rolePermissionDTO.getPermissions();
            List<String> delete = getDelete(permissionSetFromWeb, permissionSetFromDB).stream().toList();
            delete.forEach(item -> rolePermissionMapper.delete(new RolePermission(rid, item)));
            List<String> insert = getInsert(permissionSetFromWeb, permissionSetFromDB).stream().toList();
            insert.forEach(item -> rolePermissionMapper.insert(new RolePermission(rid, item)));
        }
        rolePermissionUpdateHandler(rid);
        return ResponseUtil.success();
    }
    @Transactional
    @Override
    public <T> BaseResponse<T> addRole(RoleDTO roleDTO) {
        Role role = new Role(roleDTO);
        int i = roleMapper.insert(role);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        if (!Objects.isNull(roleDTO.getPermissions()) && !roleDTO.getPermissions().isEmpty()) {
            ArrayList<RolePermission> rolePermissions = new ArrayList<>();
            String rid = role.getRid();
            roleDTO.getPermissions().forEach(item -> rolePermissions.add(new RolePermission(rid, item.getPid())));
            int j = rolePermissionMapper.insertBatch(rolePermissions);
            if (j != rolePermissions.size())
                throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        }
        return ResponseUtil.success();
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
        userRoleDeleteHandler(userRoleWrapper);
        if (rolePermissionNumber != 0) {
            int i = rolePermissionMapper.deleteByQuery(rolePermissionWrapper);
            if (i != rolePermissionNumber)
                throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        }
        if (userRoleNumber != 0) {
            int i = userRoleMapper.deleteByQuery(userRoleWrapper);
            if (i != userRoleNumber)
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
        rolePermissionDeleteHandler(rolePermissionWrapper);
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
            List<Permission> permissions = rolePermissionMapper.getRolePermission(item.getRid());
            rolePermissionList.add(new RolePermissionVO(item, permissions));
        });
        return ResponseUtil.success(rolePermissionList);
    }
    @Override
    public <T> BaseResponse<T> logout() {
        String uid = SecurityUtil.getUserPrincipal();
        if (Objects.equals(uid, "anonymousUser"))
            throw new ServiceException(ServiceExceptionEnum.UN_LOGIN);
        redisUtil.delete("uid:" + uid);
        return ResponseUtil.success();
    }
    @Override
    public BaseResponse<Page<UserDetailVO>> gets(String keyWord, int pageNo, int pageSize) {
        Page<UserDetailVO> userDetailVOPage = QueryChain.of(User.class)
                .select(USER.ALL_COLUMNS, ROLE.ALL_COLUMNS)
                .from(USER)
                .leftJoin(USER_ROLE).on(USER.UID.eq(USER_ROLE.UID))
                .leftJoin(ROLE).on(USER_ROLE.RID.eq(ROLE.RID))
                .where(USER.REAL_NAME.like(keyWord)).or(USER.USERNAME.like(keyWord))
                .pageAs(Page.of(pageNo, pageSize), UserDetailVO.class);
        return ResponseUtil.success(userDetailVOPage);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> deleteUser(String uid) {
        QueryWrapper wrapper = QueryWrapper.create().where(USER_ROLE.UID.eq(uid));
        int i = userRoleMapper.deleteByQuery(wrapper);
        if (i >= 0) {
            int j = mapper.deleteById(uid);
            if (j > 0)
                return ResponseUtil.success();
        }
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> updateUser(UpdateUserDTO updateUserDTO) {
        if (!Objects.isNull(updateUserDTO.getPassword()) && !updateUserDTO.getPassword().isEmpty())
            updateUserDTO.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        User user = new User(updateUserDTO);
        int update = mapper.update(user);
        if (update > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    public BaseResponse<List<PermissionTreeVO>> getPermissionTree() {
        List<Permission> permissions = permissionMapper.selectAll();
        PermissionTreeVO root =
                new PermissionTreeVO("root", "root", "root", new ArrayList<>());
        for (Permission permission : permissions) {
            PermissionTreeVO nowAt = root;
            int beginIndex = 0;
            String parentPrefix = "";
            while (beginIndex != -1) {
                String name, pid, desc;
                int index = permission.getPermissionName().indexOf(':', beginIndex);
                if (index == -1) {
                    name = permission.getPermissionName();
                    pid = permission.getPid();
                    desc = permission.getPermissionDesc();
                    beginIndex = -1;
                } else {
                    name = permission.getPermissionName().substring(beginIndex, index);
                    desc = name;
                    pid = parentPrefix + name + ':';
                    parentPrefix = pid;
                    beginIndex = index + 1;
                }
                PermissionTreeVO finalNowAt = nowAt;
                nowAt = nowAt.getChildren().stream()
                        .filter(tree -> tree.getPermissionName().equals(name))
                        .findFirst()
                        .orElseGet(()->{
                            PermissionTreeVO permissionTreeVO = new PermissionTreeVO(pid, name, desc, new ArrayList<>());
                            finalNowAt.getChildren().add(permissionTreeVO);
                            return permissionTreeVO;
                        }
                );
            }
        }
        return ResponseUtil.success(root.getChildren());
    }
    private void rolePermissionDeleteHandler(QueryWrapper rolePermissionWrapper) {
        List<String> rids = rolePermissionMapper.selectListByQuery(rolePermissionWrapper).stream().map(RolePermission::getRid).toList();
        rids.forEach(this::rolePermissionUpdateHandler);
    }
    private void userRoleDeleteHandler(QueryWrapper userRoleWrapper) {
        List<String> uidList = userRoleMapper.selectListByQuery(userRoleWrapper).stream().map(UserRole::getUid).toList();
        uidList.forEach(this::userRoleUpdateHandler);
    }
    private void userRoleUpdateHandler(String uid) {
        redisUtil.delete("uid:" + uid);
    }
    private void rolePermissionUpdateHandler(String rid) {
        QueryWrapper wrapper = QueryWrapper.create().from(USER_ROLE).where(USER_ROLE.RID.eq(rid));
        List<UserRole> userRoles = userRoleMapper.selectListByQuery(wrapper);
        userRoles.forEach(item -> userRoleUpdateHandler(item.getUid()));
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
    private String createToken(String redisKey, String key) {
        String uuid = UUID.randomUUID().toString();
        return JWT.create()
                .setPayload("uid", redisKey)
                .setJWTId(uuid)
                .setSubject("StudentWorkManagementSystem")
                .setIssuer("BridgeFishDev")
                .setIssuedAt(DateUtil.date())
                .setExpiresAt(Date.from(Instant.now().plusSeconds(604800)))
                .setKey(key.getBytes())
                .sign();
    }
}
