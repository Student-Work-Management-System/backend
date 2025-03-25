package edu.guet.studentworkmanagementsystem.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.RoleDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.RolePermissionDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.UserRoleDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.*;
import edu.guet.studentworkmanagementsystem.entity.po.user.Permission;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.entity.vo.authority.PermissionTreeVO;
import edu.guet.studentworkmanagementsystem.entity.vo.authority.RolePermissionDetail;
import edu.guet.studentworkmanagementsystem.entity.vo.user.FindBackPasswordVO;
import edu.guet.studentworkmanagementsystem.entity.vo.user.LoginUserVO;
import edu.guet.studentworkmanagementsystem.entity.vo.user.UserDetailInfo;

import java.util.List;

public interface UserService extends IService<User> {
    BaseResponse<LoginUserVO> login(LoginUserDTO loginUserDTO) throws JsonProcessingException;
    <T> BaseResponse<T> addUser(RegisterUser registerUser);
    <T> BaseResponse<T> addUsers(ValidateList<RegisterUser> registerUsers);
    BaseResponse<UserDetailInfo> getUserDetails(String username);
    <T> BaseResponse<T> updateUserRole(UserRoleDTO userRoleDTO);
    <T> BaseResponse<T> updateRolePermission(RolePermissionDTO rolePermissionDTO);
    <T> BaseResponse<T> addRole(RoleDTO roleDTO);
    <T> BaseResponse<T> addPermission(Permission permission);
    <T> BaseResponse<T> deleteRole(String rid);
    <T> BaseResponse<T> deletePermission(String pid);
    BaseResponse<List<RolePermissionDetail>> getAllRole();
    <T> BaseResponse<T> logout();
    BaseResponse<List<UserDetailInfo>> gets(UserQuery query);
    <T> BaseResponse<T> deleteUser(String uid);
    <T> BaseResponse<T> recoveryUser(String uid);
    <T> BaseResponse<T> updateUser(UpdateUserDTO updateUserDTO);
    BaseResponse<List<PermissionTreeVO>> getPermissionTree();
    BaseResponse<FindBackPasswordVO> findBackPassword(String username);
    <T> BaseResponse<T> updatePassword(String username, String password, String code);
}
