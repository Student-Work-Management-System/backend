package edu.guet.studentworkmanagementsystem.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.RoleRequest;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.RolePermissionRequest;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.UserRoleRequest;
import edu.guet.studentworkmanagementsystem.entity.dto.user.*;
import edu.guet.studentworkmanagementsystem.entity.po.user.Permission;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.entity.vo.authority.PermissionTreeItem;
import edu.guet.studentworkmanagementsystem.entity.vo.authority.RolePermissionDetail;
import edu.guet.studentworkmanagementsystem.entity.vo.user.FindBackPasswordItem;
import edu.guet.studentworkmanagementsystem.entity.vo.user.LoginUserDetail;
import edu.guet.studentworkmanagementsystem.entity.vo.user.UserDetailItem;

import java.util.List;

public interface UserService extends IService<User> {
    BaseResponse<LoginUserDetail> login(LoginUserRequest loginUserRequest) throws JsonProcessingException;
    <T> BaseResponse<T> addUser(RegisterUserRequest registerUserRequest);
    <T> BaseResponse<T> addUsers(ValidateList<RegisterUserRequest> registerUserRequests);
    BaseResponse<UserDetailItem> getUserDetails(String username);
    <T> BaseResponse<T> updateUserRole(UserRoleRequest userRoleRequest);
    <T> BaseResponse<T> updateRolePermission(RolePermissionRequest rolePermissionRequest);
    <T> BaseResponse<T> addRole(RoleRequest roleItem);
    <T> BaseResponse<T> addPermission(Permission permission);
    <T> BaseResponse<T> deleteRole(String rid);
    <T> BaseResponse<T> deletePermission(String pid);
    BaseResponse<List<RolePermissionDetail>> getAllRole();
    <T> BaseResponse<T> logout();
    BaseResponse<List<UserDetailItem>> gets(UserQuery query);
    <T> BaseResponse<T> deleteUser(String uid);
    <T> BaseResponse<T> recoveryUser(String uid);
    <T> BaseResponse<T> updateUser(UpdateUserRequest updateUserRequest);
    BaseResponse<List<PermissionTreeItem>> getPermissionTree();
    BaseResponse<FindBackPasswordItem> findBackPassword(String username);
    <T> BaseResponse<T> updatePassword(String username, String password, String code);
}
