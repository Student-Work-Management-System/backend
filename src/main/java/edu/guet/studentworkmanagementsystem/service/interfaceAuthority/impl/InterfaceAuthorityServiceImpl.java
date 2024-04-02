package edu.guet.studentworkmanagementsystem.service.interfaceAuthority.impl;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.vo.authority.InterfaceAuthority;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.service.interfaceAuthority.InterfaceAuthorityService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InterfaceAuthorityServiceImpl implements InterfaceAuthorityService {
    private static final ArrayList<String> prefixes = new ArrayList<>(){{
        add("user");
        add("auth");
        add("major");
        add("student");
        add("status");
        add("leave");
    }};
    private static final ArrayList<InterfaceAuthority> userAuthority = new ArrayList<>(){{
        add(new InterfaceAuthority("/login", null));
        add(new InterfaceAuthority("/add", List.of("user:insert", "user_role:insert")));
        add(new InterfaceAuthority("/adds", List.of("user:insert", "user_role:insert")));
        add(new InterfaceAuthority("/details", List.of("user:select", "user_role:select")));
        add(new InterfaceAuthority("/gets", List.of("user:select", "user_role:select")));
        add(new InterfaceAuthority("/update/role", List.of("user:update:all", "user_role:insert", "user_role:delete", "role:select")));
        add(new InterfaceAuthority("/delete", List.of("user:delete", "user_role:delete")));
        add(new InterfaceAuthority("/update", List.of("user:update:all")));
        add(new InterfaceAuthority("/logout", null));
    }};

    private static final ArrayList<InterfaceAuthority> permissionAuthority = new ArrayList<>(){{
        add(new InterfaceAuthority("/gets", List.of("role:select", "role_permission:select")));
        add(new InterfaceAuthority("/permission/gets", List.of("permission:select")));
        add(new InterfaceAuthority("/update/role/permission", List.of("role_permission:insert", "role_permission:delete", "permission:select")));
        add(new InterfaceAuthority("/add/role", List.of("role:insert", "role_permission:insert")));
        add(new InterfaceAuthority("/add/permission", List.of("permission:insert")));
        add(new InterfaceAuthority("/delete/role", List.of("role:delete", "user_role:delete", "role_permission:delete")));
        add(new InterfaceAuthority("/delete/permission", List.of("permission:delete", "role_permission:delete")));
    }};
    private static final ArrayList<InterfaceAuthority> leaveAuthority = new ArrayList<>(){{
        add(new InterfaceAuthority("/gets", List.of("student:select", "student_leave:select", "student_leave_audit:select", "major:select", "user:select")));
        add(new InterfaceAuthority("/adds", List.of("student_leave:insert", "student_leave_audit:insert")));
        add(new InterfaceAuthority("/add", List.of("student_leave:insert", "student_leave_audit:insert")));
        add(new InterfaceAuthority("/audit", List.of("student_leave_audit:update")));
        add(new InterfaceAuthority("/update", List.of("student_leave:update")));
        add(new InterfaceAuthority("/delete", List.of("student_leave:delete", "student:leave_audit:delete")));
    }};
    private static final ArrayList<InterfaceAuthority> majorAuthority = new ArrayList<>(){{
        add(new InterfaceAuthority("/gets", List.of("major:select")));
        add(new InterfaceAuthority("/add", List.of("major:insert")));
        add(new InterfaceAuthority("/update", List.of("major:update")));
        add(new InterfaceAuthority("/delete", List.of("major:delete")));
    }};
    private static final ArrayList<InterfaceAuthority> statusAuthority = new ArrayList<>(){{
        add(new InterfaceAuthority("/gets", List.of("student_status:select", "student:select")));
        add(new InterfaceAuthority("/add", List.of("student_status:insert")));
        add(new InterfaceAuthority("/adds", List.of("student_status:insert")));
        add(new InterfaceAuthority("/update", List.of("student_status:update")));
        add(new InterfaceAuthority("/delete", List.of("student_status:delete")));
    }};
    private static final ArrayList<InterfaceAuthority> studentAuthority = new ArrayList<>(){{
        add(new InterfaceAuthority("/gets", List.of("student:select")));
        add(new InterfaceAuthority("/add", List.of("student:insert", "user:insert", "user_role:insert")));
        add(new InterfaceAuthority("/adds", List.of("student:insert", "user:insert", "user_role:insert")));
        add(new InterfaceAuthority("/update", List.of("student:update")));
        add(new InterfaceAuthority("/delete", List.of("student:delete", "user:delete", "user_role:delete")));
    }};
    @Override
    public BaseResponse<List<InterfaceAuthority>> getInterfaceAuthorities(String prefix) {
        switch (prefix) {
            case "user" -> {
                return getUser();
            }
            case "auth" -> {
                return getAuth();
            }
            case "leave" -> {
                return getLeave();
            }
            case "major" -> {
                return getMajor();
            }
            case "status" -> {
                return getStatus();
            }
            case "student" -> {
                return getStudent();
            }
            default -> throw new ServiceException(ServiceExceptionEnum.SELECT_NOT_IN);
        }
    }
    @Override
    public BaseResponse<List<String>> getOptional() {
        return ResponseUtil.success(prefixes);
    }
    private BaseResponse<List<InterfaceAuthority>> getUser() {
        return ResponseUtil.success(userAuthority);
    }
    private BaseResponse<List<InterfaceAuthority>> getAuth() {
        return ResponseUtil.success(permissionAuthority);
    }
    private BaseResponse<List<InterfaceAuthority>> getLeave() {
        return ResponseUtil.success(leaveAuthority);
    }
    private BaseResponse<List<InterfaceAuthority>> getMajor() {
        return ResponseUtil.success(majorAuthority);
    }
    private BaseResponse<List<InterfaceAuthority>> getStatus() {
        return ResponseUtil.success(statusAuthority);
    }
    private BaseResponse<List<InterfaceAuthority>> getStudent() {
        return ResponseUtil.success(studentAuthority);
    }
}
