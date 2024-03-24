package edu.guet.studentworkmanagementsystem.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.RolePermissionDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.UserRoleDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.LoginUserDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUserDTO;
import edu.guet.studentworkmanagementsystem.entity.po.user.Permission;
import edu.guet.studentworkmanagementsystem.entity.po.user.Role;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.entity.vo.authority.RolePermissionVO;
import edu.guet.studentworkmanagementsystem.entity.vo.user.LoginUserVO;
import edu.guet.studentworkmanagementsystem.entity.vo.user.UserDetailVO;

import java.util.List;

public interface UserService extends IService<User> {
    BaseResponse<LoginUserVO> login(LoginUserDTO loginUserDTO) throws JsonProcessingException;
    <T> BaseResponse<T> addUser(RegisterUserDTO registerUserDTO);
    <T> BaseResponse<T> addUsers(List<RegisterUserDTO> registerUserDTOList);
    BaseResponse<UserDetailVO> getUserDetails(String username);
    <T> BaseResponse<T> updateUserRole(UserRoleDTO userRoleDTO);
    <T> BaseResponse<T> updateRolePermission(RolePermissionDTO rolePermissionDTO);
    <T> BaseResponse<T> addRole(Role role);
    <T> BaseResponse<T> addPermission(Permission permission);
    <T> BaseResponse<T> deleteRole(String rid);
    <T> BaseResponse<T> deletePermission(String pid);
    BaseResponse<List<RolePermissionVO>> getAllRole();
    <T> BaseResponse<T> logout();
}
