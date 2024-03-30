package edu.guet.studentworkmanagementsystem;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.RolePermissionDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.UserRoleDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.LoginUserDTO;
import edu.guet.studentworkmanagementsystem.entity.vo.authority.RolePermissionVO;
import edu.guet.studentworkmanagementsystem.entity.vo.user.LoginUserVO;
import edu.guet.studentworkmanagementsystem.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;


@SpringBootTest
class StudentWorkManagementSystemApplicationTests {
    @Autowired
    private UserService userService;
    @Test
    void contextLoads() throws JsonProcessingException {
        // login();
    }
    private void updateUserRole() {
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUid("23");
        HashSet<String> set = new HashSet<>();
        set.add("3");
        set.add("4");
        userRoleDTO.setRoles(set);
        BaseResponse<Object> baseResponse = userService.updateUserRole(userRoleDTO);
        System.out.println(baseResponse);
    }
    private void deleteRole() {
        BaseResponse<Object> baseResponse = userService.deleteRole("3");
        System.out.println(baseResponse);
    }
    private void deletePermission() {
        BaseResponse<Object> baseResponse = userService.deletePermission("999");
        System.out.println(baseResponse);
    }
    private void updateRolePermission() {
        RolePermissionDTO rolePermissionDTO = new RolePermissionDTO();
        rolePermissionDTO.setRid("3");
        HashSet<String> set = new HashSet<>();
        set.add("998");
        set.add("999");
        rolePermissionDTO.setPermissions(set);
        BaseResponse<Object> baseResponse = userService.updateRolePermission(rolePermissionDTO);
        System.out.println(baseResponse);
    }
    private void login() throws JsonProcessingException {
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setUsername("testUser");
        loginUserDTO.setPassword("testUser");
        BaseResponse<LoginUserVO> login = userService.login(loginUserDTO);
        System.out.println(login);
    }
    private void getAllRole() {
        BaseResponse<List<RolePermissionVO>> allRole = userService.getAllRole();
        System.out.println(allRole.getData());
    }
}
