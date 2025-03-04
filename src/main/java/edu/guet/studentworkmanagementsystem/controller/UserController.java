package edu.guet.studentworkmanagementsystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.UserRoleDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.*;
import edu.guet.studentworkmanagementsystem.entity.vo.user.FindBackPasswordVO;
import edu.guet.studentworkmanagementsystem.entity.vo.user.LoginUserVO;
import edu.guet.studentworkmanagementsystem.entity.vo.user.UserDetailVO;
import edu.guet.studentworkmanagementsystem.service.user.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PermitAll
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> login(@RequestBody @Valid LoginUserDTO loginUserDTO) throws JsonProcessingException {
        return userService.login(loginUserDTO);
    }
    @PreAuthorize("hasAuthority('user:insert') and hasAuthority('user_role:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> addUser(@RequestBody @Valid RegisterUser registerUser) {
        return userService.addUser(registerUser);
    }
    @PreAuthorize("hasAuthority('user:insert') and hasAuthority('user_role:insert')")
    @PostMapping("/adds")
    public <T> BaseResponse<T> addUsers(@RequestBody @Valid ValidateList<RegisterUser> registerUsers) {
        return userService.addUsers(registerUsers);
    }
    @PreAuthorize("hasAuthority('user:select') and hasAuthority('user_role:select')")
    @GetMapping("/detail/{username}")
    public BaseResponse<UserDetailVO> getUserDetails(@PathVariable String username) {
        return userService.getUserDetails(username);
    }
    @PreAuthorize("hasAuthority('user:select') and hasAuthority('user_role:select')")
    @PostMapping("/gets")
    public BaseResponse<List<UserDetailVO>> gets(@RequestBody UserQuery query) {
        return userService.gets(query);
    }
    @PreAuthorize(
            "hasAuthority('user:update:all') " +
            "and hasAuthority('user_role:insert') " +
            "and hasAuthority('user_role:delete') " +
            "and hasAuthority('role:select')"
    )
    @PutMapping("/update/role")
    public <T> BaseResponse<T> updateUserRole(@RequestBody @Valid UserRoleDTO userRoleDTO) {
        return userService.updateUserRole(userRoleDTO);
    }
    @PreAuthorize("hasAuthority('user:delete') and hasAuthority('user_role:delete')")
    @DeleteMapping("/delete/{uid}")
    public <T> BaseResponse<T> deleteUser(@PathVariable String uid) {
        return userService.deleteUser(uid);
    }
    @PreAuthorize("hasAuthority('user:update:all')")
    @PutMapping("/recovery/{uid}")
    public <T> BaseResponse<T> recoveryUser(@PathVariable String uid) {
        return userService.recoveryUser(uid);
    }
    @PreAuthorize("hasAuthority('user:update:all')")
    @PutMapping("/update")
    public <T> BaseResponse<T> updateUser(@RequestBody @Valid UpdateUserDTO updateUserDTO) {
        return userService.updateUser(updateUserDTO);
    }
    @PermitAll
    @DeleteMapping("/logout")
    public <T> BaseResponse<T> logout() {
        return userService.logout();
    }
    @PermitAll
    @GetMapping("/findBackPassword/{username}")
    public BaseResponse<FindBackPasswordVO> findBackPassword(@PathVariable String username) {
        return userService.findBackPassword(username);
    }
    @PermitAll
    @PostMapping("/updatePassword")
    public <T> BaseResponse<T> updatePassword(@RequestBody @Valid UpdatePassword updatePassword) {
        return userService.updatePassword(updatePassword.getUsername(), updatePassword.getPassword(), updatePassword.getCode());
    }
}
