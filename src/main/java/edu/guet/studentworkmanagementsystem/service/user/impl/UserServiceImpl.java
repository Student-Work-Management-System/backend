package edu.guet.studentworkmanagementsystem.service.user.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.Common;
import edu.guet.studentworkmanagementsystem.common.UsernameTrie;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.RoleRequest;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.RolePermissionRequest;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.UserRoleRequest;
import edu.guet.studentworkmanagementsystem.entity.dto.user.*;
import edu.guet.studentworkmanagementsystem.entity.po.other.Grade;
import edu.guet.studentworkmanagementsystem.entity.po.user.*;
import edu.guet.studentworkmanagementsystem.entity.vo.authority.PermissionTreeItem;
import edu.guet.studentworkmanagementsystem.entity.vo.authority.RolePermissionDetail;
import edu.guet.studentworkmanagementsystem.entity.vo.user.FindBackPasswordItem;
import edu.guet.studentworkmanagementsystem.entity.vo.user.LoginUserDetail;
import edu.guet.studentworkmanagementsystem.entity.vo.user.UserDetailItem;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.authority.RolePermissionMapper;
import edu.guet.studentworkmanagementsystem.mapper.authority.UserRoleMapper;
import edu.guet.studentworkmanagementsystem.mapper.user.PermissionMapper;
import edu.guet.studentworkmanagementsystem.mapper.user.RoleMapper;
import edu.guet.studentworkmanagementsystem.mapper.user.UserMapper;
import edu.guet.studentworkmanagementsystem.securiy.SecurityUser;
import edu.guet.studentworkmanagementsystem.securiy.SystemAuthority;
import edu.guet.studentworkmanagementsystem.service.email.EmailService;
import edu.guet.studentworkmanagementsystem.service.other.OtherService;
import edu.guet.studentworkmanagementsystem.service.user.UserService;
import edu.guet.studentworkmanagementsystem.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static edu.guet.studentworkmanagementsystem.entity.po.other.table.CounselorTableDef.COUNSELOR;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.GradeTableDef.GRADE;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.PermissionTableDef.PERMISSION;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.RolePermissionTableDef.ROLE_PERMISSION;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.RoleTableDef.ROLE;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.UserRoleTableDef.USER_ROLE;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.UserTableDef.USER;

@Service
@Slf4j
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
    @Autowired
    private EmailService emailService;
    @Value("${jwt.key}")
    private String key;
    @Qualifier("readThreadPool")
    @Autowired
    private ThreadPoolTaskExecutor readThreadPool;
    @Autowired
    private OtherService otherService;

    @Override
    public BaseResponse<LoginUserDetail> login(LoginUserRequest loginUserRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserRequest.getUsername(), loginUserRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        SecurityUser securityUser = (SecurityUser) authenticate.getPrincipal();
        String redisKey = Common.LOGIN_UID.getValue() + securityUser.getUser().getUid();
        String token = createToken(redisKey, key);
        try {
            redisUtil.setValue(redisKey, JsonUtil.getMapper().writeValueAsString(securityUser));
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("UserServiceImpl#login(LoginUserDTO loginUserDTO)出现JSON解析异常: {}", jsonProcessingException.getMessage());
            ResponseUtil.failure(ServiceExceptionEnum.OPERATE_ERROR);
        }
        LoginUserDetail loginUserDetail = createLoginUser(securityUser, token);
        return ResponseUtil.success(loginUserDetail);
    }

    public LoginUserDetail createLoginUser(SecurityUser securityUser, String token) {
        List<SystemAuthority> authorities = (List<SystemAuthority>) securityUser.getAuthorities();
        LoginUserDetail loginUser = LoginUserDetail.builder()
                .uid(securityUser.getUser().getUid())
                .username(securityUser.getUsername())
                .email(securityUser.getUser().getEmail())
                .authorities(authorities)
                .token(token)
                .build();
        // 检查权限:
        // 1. 若有student:status权限, 说明至少有辅导员身份, 可以查看该辅导员负责的年级和学历层次学生
        // 2. 若有student:status:all权限, 说明至少是辅导员以上身份, 可以查看所有的学生信息
        Set<String> set = authorities.stream().map(SystemAuthority::getAuthority).collect(Collectors.toSet());
        boolean hasStudentStatusPermission = hasStudentStatusPermission(set);
        boolean hasStudentStatusAllPermission = hasStudentStatusAllPermission(set);
        //  两权限均无, 则无需查询相关信息
        if (!hasStudentStatusPermission && !hasStudentStatusAllPermission)
            return loginUser;
        // 有student:status, 则查询负责信息, 在登录后返回给前端保存
        if (hasStudentStatusPermission && !hasStudentStatusAllPermission) {
            String uid = securityUser.getUser().getUid();
            loginUser.setChargeGrades(getChargeGrade(uid));
            return loginUser;
        }
        // 有student:status:all, 则在前端保存所有的年级和学历层次信息
        loginUser.setChargeGrades(otherService.getGradeList());
        return loginUser;
    }

    public List<Grade> getChargeGrade(String uid) {
        return QueryChain.of(Grade.class)
                .select(GRADE.ALL_COLUMNS)
                .innerJoin(COUNSELOR).on(COUNSELOR.GRADE_ID.eq(GRADE.GRADE_ID))
                .innerJoin(USER).on(USER.UID.eq(USER.UID))
                .where(USER.UID.eq(uid))
                .list();
    }

    public boolean hasStudentStatusAllPermission(Set<String> permissions) {
        return permissions.contains(Common.STATUS_PERMISSION.getValue());
    }

    public boolean hasStudentStatusPermission(Set<String> permissions) {
        return permissions.contains(Common.STATUS_PERMISSION.getValue());
    }

    private boolean RoleNotNullOrEmpty(Set<String> roles) {
        return !Objects.isNull(roles) && !roles.isEmpty();
    }

    @Transactional
    public void addUserRole(Set<String> roles, String uid) {
        ArrayList<UserRole> userRoles = new ArrayList<>();
        roles.forEach(item -> userRoles.add(new UserRole(uid, item)));
        int i = userRoleMapper.insertBatch(userRoles);
        if (i == roles.size())
            return;
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Transactional
    @Override
    public <T> BaseResponse<T> addUser(RegisterUserRequest registerUserRequest) {
        ValidateList<RegisterUserRequest> registerUserRequests = new ValidateList<>();
        registerUserRequests.add(registerUserRequest);
        return addUsers(registerUserRequests);
    }

    @Transactional
    @Override
    public <T> BaseResponse<T> addUsers(ValidateList<RegisterUserRequest> registerUserRequests) {
        checkAccountExisted(registerUserRequests);
        List<User> users = insertUserBatch(registerUserRequests);
        List<UserRoleRequest> userRoleRequests = matchUserRole(registerUserRequests.size(), registerUserRequests, users);
        addUserRoleBatch(userRoleRequests);
        return ResponseUtil.success();
    }

    @Transactional
    public void checkAccountExisted(List<RegisterUserRequest> registerUserRequests) {
        // 从数据库中检查是否有重复的用户
        // 学生 -> username是学号, 系统唯一
        // 其他身份 -> 默认使用工号(待商榷)
        List<String> usernames = registerUserRequests.stream()
                .map(RegisterUserRequest::getUsername)
                .toList();
        List<User> userFromDB = QueryChain.of(User.class)
                .where(USER.USERNAME.in(usernames))
                .list();
        if (!userFromDB.isEmpty()) {
            throw new ServiceException(ServiceExceptionEnum.ACCOUNT_EXISTED);
        }
    }

    @Transactional
    public List<User> insertUserBatch(List<RegisterUserRequest> registerUserRequests) {
        // 构造数据库结构的User并插入
        ArrayList<User> users = new ArrayList<>();
        registerUserRequests.forEach(it -> {
            User user = createUser(it);
            users.add(user);
        });
        // mapper.insertBatch(List<Entity> list)无法像mapper.insert(Entity)一样插入后写回id, 因此无法使用
        // 需要使用Db.execute写法, 此写法是通过Statement.executeBatch()进行批量执行, 数量大时效率高
        boolean flag = DbInsertUserBatch(users);
        if (!flag)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return users;
    }

    public boolean DbInsertUserBatch(List<User> users) {
        int size = users.size();
        int[] executedResult = Db.executeBatch(users, 1000, UserMapper.class, BaseMapper::insert);
        int sum = 0;
        for (int item : executedResult) {
            sum += item;
        }
        return sum == size;
    }

    @Transactional
    public List<UserRoleRequest> matchUserRole(int size, List<RegisterUserRequest> registerUserRequests, List<User> users) {
        // username和对应身份配对
        UsernameTrie root = new UsernameTrie();
        root.buildTrie(registerUserRequests);
        ArrayList<UserRoleRequest> userRoleRequests = new ArrayList<>();
        for (int idx = 0; idx < size; idx++) {
            User user = users.get(idx);
            String username = user.getUsername();
            String uid = user.getUid();
            if (!root.search(username))
                continue;
            UsernameTrie.TrieNode node = root.getNode(username);
            UserRoleRequest userRoleRequest = createUserRoleDTO(uid, node.getRoles());
            userRoleRequests.add(userRoleRequest);
        }
        return userRoleRequests;
    }

    public User createUser(RegisterUserRequest registerUserRequest) {
        return User.builder()
                .username(registerUserRequest.getUsername())
                .realName(registerUserRequest.getRealName())
                .email(registerUserRequest.getEmail())
                .phone(registerUserRequest.getPhone())
                .password(passwordEncoder.encode(registerUserRequest.getPassword()))
                .createdAt(LocalDate.now())
                .enabled(true)
                .build();
    }

    public UserRoleRequest createUserRoleDTO(String uid, Set<String> roles) {
        return UserRoleRequest.builder()
                .uid(uid)
                .roles(roles)
                .build();
    }

    @Transactional
    public void addUserRoleBatch(List<UserRoleRequest> userRoleRequests) {
        userRoleRequests.forEach(it -> {
            if (RoleNotNullOrEmpty(it.getRoles()))
                addUserRole(it.getRoles(), it.getUid());
        });
    }

    @Override
    // @Cacheable(value = "userDetailCache", key = "username")
    public BaseResponse<UserDetailItem> getUserDetails(String username) {
        CompletableFuture<UserDetailItem> future = CompletableFuture.supplyAsync(() -> {
            UserDetailItem user = QueryChain.of(User.class)
                    .and(USER.USERNAME.eq(username))
                    .oneAs(UserDetailItem.class);
            if (Objects.isNull(user))
                throw new ServiceException(ServiceExceptionEnum.ACCOUNT_NOT_FOUND);
            String uid = user.getUid();
            List<Role> userRole = userRoleMapper.getUserRole(uid);
            if (!Objects.isNull(userRole))
                user.setRoles(userRole);
            return user;
        }, readThreadPool);
        UserDetailItem info = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(info);
    }

    /**
     * 检查传入的列表是否为空 或 传入的列表内全为空(当该用户没有角色 或 该角色没有权限 时会出现该情况)
     */
    private <T> boolean listHandler(List<T> list) {
        if (Objects.isNull(list) || list.isEmpty())
            return true;
        return list.size() == 1 && Objects.isNull(list.getFirst());
    }

    @Transactional
    @Override
    public <T> BaseResponse<T> updateUserRole(UserRoleRequest userRoleRequest) {
        String uid = userRoleRequest.getUid();
        List<Role> roles = userRoleMapper.getUserRole(uid);
        if (listHandler(roles)) {
            addUserRole(userRoleRequest.getRoles(), uid);
        } else {
            Set<String> roleSetFromDB = roles.stream()
                    .map(Role::getRid)
                    .filter(StringUtils::hasLength)
                    .collect(Collectors.toSet());
            Set<String> roleSetFromWeb = userRoleRequest.getRoles();
            List<String> delete = getDelete(roleSetFromWeb, roleSetFromDB).stream().toList();
            delete.forEach(item -> userRoleMapper.delete(new UserRole(uid, item)));
            List<String> insert = getInsert(roleSetFromWeb, roleSetFromDB).stream().toList();
            insert.forEach(item -> userRoleMapper.insert(new UserRole(uid, item)));
        }
        userRoleUpdateHandler(uid);
        return ResponseUtil.success();
    }

    @Transactional
    public void addRolePermission(List<String> permissions, String rid) {
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
    public <T> BaseResponse<T> updateRolePermission(RolePermissionRequest rolePermissionRequest) {
        String rid = rolePermissionRequest.getRid();
        List<Permission> permissions = rolePermissionMapper.getRolePermission(rid);
        if (listHandler(permissions)) {
            addRolePermission(rolePermissionRequest.getPermissions().stream().toList(), rid);
        } else {
            Set<String> permissionSetFromDB = permissions.stream()
                    .map(Permission::getPid)
                    .filter(StringUtils::hasLength)
                    .collect(Collectors.toSet());
            Set<String> permissionSetFromWeb = rolePermissionRequest.getPermissions();
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
    public <T> BaseResponse<T> addRole(RoleRequest roleItem) {
        Role role = new Role(roleItem);
        int i = roleMapper.insert(role);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        if (!Objects.isNull(roleItem.getPermissions()) && !roleItem.getPermissions().isEmpty()) {
            ArrayList<RolePermission> rolePermissions = new ArrayList<>();
            String rid = role.getRid();
            roleItem.getPermissions().forEach(item -> rolePermissions.add(new RolePermission(rid, item.getPid())));
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
    public BaseResponse<List<RolePermissionDetail>> getAllRole() {
        CompletableFuture<List<RolePermissionDetail>> future = CompletableFuture.supplyAsync(() -> {
            List<Role> roles = roleMapper.selectAll();
            ArrayList<RolePermissionDetail> rolePermissionDetailList = new ArrayList<>();
            roles.forEach(item -> {
                List<Permission> permissions = rolePermissionMapper.getRolePermission(item.getRid());
                rolePermissionDetailList.add(new RolePermissionDetail(item, permissions));
            });
            return rolePermissionDetailList;
        }, readThreadPool);
        List<RolePermissionDetail> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    public <T> BaseResponse<T> logout() {
        String uid = SecurityUtil.getUserPrincipal();
        if (Objects.equals(uid, Common.ANONYMOUS_USER.getValue()))
            throw new ServiceException(ServiceExceptionEnum.UN_LOGIN);
        redisUtil.delete("uid:" + uid);
        return ResponseUtil.success();
    }

    @Override
    public BaseResponse<List<UserDetailItem>> gets(UserQuery query) {
        CompletableFuture<List<UserDetailItem>> future = CompletableFuture.supplyAsync(() -> QueryChain.of(User.class)
                        .select(USER.ALL_COLUMNS, ROLE.ALL_COLUMNS)
                        .from(USER)
                        .leftJoin(USER_ROLE).on(USER.UID.eq(USER_ROLE.UID))
                        .leftJoin(ROLE).on(USER_ROLE.RID.eq(ROLE.RID))
                        .where(USER.ENABLED.eq(query.getEnabled()))
                        .and(USER.REAL_NAME.like(query.getKeyword()).or(USER.USERNAME.like(query.getKeyword())))
                        .listAs(UserDetailItem.class)
                , readThreadPool);
        List<UserDetailItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteUser(String uid) {
        boolean update = UpdateChain.of(User.class)
                .set(USER.ENABLED, false)
                .where(USER.UID.eq(uid))
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> recoveryUser(String uid) {
        boolean update = UpdateChain.of(User.class)
                .set(USER.ENABLED, true)
                .where(USER.UID.eq(uid))
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateUser(UpdateUserRequest updateUserRequest) {
        boolean update = UpdateChain.of(User.class)
                .set(USER.REAL_NAME, updateUserRequest.getRealName(), StringUtils::hasLength)
                .set(USER.EMAIL, updateUserRequest.getEmail(), StringUtils::hasLength)
                .set(USER.PHONE, updateUserRequest.getPhone(), StringUtils::hasLength)
                .set(USER.PASSWORD, passwordEncoder.encode(updateUserRequest.getPassword()), StringUtils.hasLength(updateUserRequest.getPassword()))
                .where(USER.UID.eq(updateUserRequest.getUid()))
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<List<PermissionTreeItem>> getPermissionTree() {
        CompletableFuture<BaseResponse<List<PermissionTreeItem>>> future = CompletableFuture.supplyAsync(() -> {
            List<Permission> permissions = permissionMapper.selectAll();
            PermissionTreeItem root =
                    new PermissionTreeItem("root", "root", "root", new ArrayList<>());
            for (Permission permission : permissions) {
                PermissionTreeItem nowAt = root;
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
                    PermissionTreeItem finalNowAt = nowAt;
                    nowAt = nowAt.getChildren().stream()
                            .filter(tree -> tree.getPermissionName().equals(name))
                            .findFirst()
                            .orElseGet(() -> {
                                        PermissionTreeItem permissionTreeItem = new PermissionTreeItem(pid, name, desc, new ArrayList<>());
                                        finalNowAt.getChildren().add(permissionTreeItem);
                                        return permissionTreeItem;
                                    }
                            );
                }
            }
            return ResponseUtil.success(root.getChildren());
        }, readThreadPool);
        try {
            return future.get(3, TimeUnit.SECONDS);
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

    @Override
    public BaseResponse<FindBackPasswordItem> findBackPassword(String username) {
        User user = QueryChain.of(User.class)
                .select(USER.ALL_COLUMNS)
                .where(USER.USERNAME.eq(username))
                .one();
        if (Objects.isNull(user))
            throw new ServiceException(ServiceExceptionEnum.ACCOUNT_NOT_FOUND);
        String code = String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));
        emailService.sendEmail(user.getEmail(), code);
        redisUtil.setValue(Common.UPDATE_PASSWORD.getValue() + username, code, 5);

        String email = user.getEmail();
        int idx = email.indexOf('@');
        String suffix = email.substring(idx);
        int maskLength = idx + 1 - 3 - 3;
        String maskEmail = email.substring(0, 3) + String.valueOf('*').repeat(maskLength)
                + email.substring(idx-3, idx) + suffix;
        FindBackPasswordItem findBackPasswordItem = new FindBackPasswordItem(maskEmail);
        return ResponseUtil.success(findBackPasswordItem);
    }

    @Override
    public <T> BaseResponse<T> updatePassword(String username, String password, String code) {
        String passwordRedisKey = Common.UPDATE_PASSWORD.getValue() + username;
        String codeFromRedis = String.valueOf(redisUtil.getValue(passwordRedisKey));
        if (!code.equals(codeFromRedis))
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        boolean update = UpdateChain.of(User.class)
                .set(USER.PASSWORD, passwordEncoder.encode(password), StringUtils::hasLength)
                .where(USER.USERNAME.eq(username))
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
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
        String redisUidKey = Common.LOGIN_UID.getValue() + uid;
        if (redisUtil.exists(redisUidKey))
            redisUtil.delete(redisUidKey);
    }
    private void rolePermissionUpdateHandler(String rid) {
        QueryWrapper wrapper = QueryWrapper.create().from(USER_ROLE).where(USER_ROLE.RID.eq(rid));
        List<UserRole> userRoles = userRoleMapper.selectListByQuery(wrapper);
        // 更新完毕角色权限时, 需要拥有该角色的用户重新登录保证安全
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
