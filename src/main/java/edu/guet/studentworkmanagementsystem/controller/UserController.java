package edu.guet.studentworkmanagementsystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.UserRoleRequest;
import edu.guet.studentworkmanagementsystem.entity.dto.user.*;
import edu.guet.studentworkmanagementsystem.entity.vo.user.FindBackPasswordItem;
import edu.guet.studentworkmanagementsystem.entity.vo.user.LoginUserDetail;
import edu.guet.studentworkmanagementsystem.entity.vo.user.UserDetailItem;
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
    public BaseResponse<LoginUserDetail> login(@RequestBody @Valid LoginUserRequest loginUserRequest) throws JsonProcessingException {
        return userService.login(loginUserRequest);
    }
    @PreAuthorize("hasAuthority('user:insert') and hasAuthority('user_role:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> addUser(@RequestBody @Valid RegisterUserRequest registerUserRequest) {
        return userService.addUser(registerUserRequest);
    }
    @PreAuthorize("hasAuthority('user:insert') and hasAuthority('user_role:insert')")
    @PostMapping("/adds")
    public <T> BaseResponse<T> addUsers(@RequestBody @Valid ValidateList<RegisterUserRequest> registerUserRequests) {
        return userService.addUsers(registerUserRequests);
    }
    @PreAuthorize("hasAuthority('user:select') and hasAuthority('user_role:select')")
    @GetMapping("/detail/{username}")
    public BaseResponse<UserDetailItem> getUserDetails(@PathVariable String username) {
        return userService.getUserDetails(username);
    }
    @PreAuthorize("hasAuthority('user:select') and hasAuthority('user_role:select')")
    @PostMapping("/gets")
    public BaseResponse<List<UserDetailItem>> gets(@RequestBody UserQuery query) {
        return userService.gets(query);
    }
    @PreAuthorize(
            "hasAuthority('user:update:all') " +
            "and hasAuthority('user_role:insert') " +
            "and hasAuthority('user_role:delete') " +
            "and hasAuthority('role:select')"
    )
    @PutMapping("/update/role")
    public <T> BaseResponse<T> updateUserRole(@RequestBody @Valid UserRoleRequest userRoleRequest) {
        return userService.updateUserRole(userRoleRequest);
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
    public <T> BaseResponse<T> updateUser(@RequestBody @Valid UpdateUserRequest updateUserRequest) {
        return userService.updateUser(updateUserRequest);
    }
    @PermitAll
    @DeleteMapping("/logout")
    public <T> BaseResponse<T> logout() {
        return userService.logout();
    }
    @PermitAll
    @GetMapping("/findBackPassword/{username}")
    public BaseResponse<FindBackPasswordItem> findBackPassword(@PathVariable String username) {
        return userService.findBackPassword(username);
    }
    @PermitAll
    @PostMapping("/updatePassword")
    public <T> BaseResponse<T> updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest) {
        return userService.updatePassword(updatePasswordRequest.getUsername(), updatePasswordRequest.getPassword(), updatePasswordRequest.getCode());
    }
}
