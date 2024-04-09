package edu.guet.studentworkmanagementsystem.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.RoleDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.RolePermissionDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.UserRoleDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.LoginUserDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUserDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUserDTOList;
import edu.guet.studentworkmanagementsystem.entity.dto.user.UpdateUserDTO;
import edu.guet.studentworkmanagementsystem.entity.po.user.Permission;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.entity.vo.authority.PermissionTreeVO;
import edu.guet.studentworkmanagementsystem.entity.vo.authority.RolePermissionVO;
import edu.guet.studentworkmanagementsystem.entity.vo.user.LoginUserVO;
import edu.guet.studentworkmanagementsystem.entity.vo.user.UserDetailVO;

import java.util.List;

public interface UserService extends IService<User> {
    BaseResponse<LoginUserVO> login(LoginUserDTO loginUserDTO) throws JsonProcessingException;
    <T> BaseResponse<T> addUser(RegisterUserDTO registerUserDTO);
    <T> BaseResponse<T> addUsers(RegisterUserDTOList registerUserDTOS);
    BaseResponse<UserDetailVO> getUserDetails(String username);
    <T> BaseResponse<T> updateUserRole(UserRoleDTO userRoleDTO);
    <T> BaseResponse<T> updateRolePermission(RolePermissionDTO rolePermissionDTO);
    <T> BaseResponse<T> addRole(RoleDTO roleDTO);
    <T> BaseResponse<T> addPermission(Permission permission);
    <T> BaseResponse<T> deleteRole(String rid);
    <T> BaseResponse<T> deletePermission(String pid);
    BaseResponse<List<RolePermissionVO>> getAllRole();
    <T> BaseResponse<T> logout();
    BaseResponse<Page<UserDetailVO>> gets(String keyWord, int pageNo, int pageSize);
    <T> BaseResponse<T> deleteUser(String uid);
    <T> BaseResponse<T> updateUser(UpdateUserDTO updateUserDTO);
    BaseResponse<List<PermissionTreeVO>> getPermissionTree();
    <T> BaseResponse<T> findBackPassword(String username);
    <T> BaseResponse<T> updatePassword(String username, String password, String code);
}
