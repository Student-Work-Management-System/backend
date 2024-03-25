package edu.guet.studentworkmanagementsystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.UserRoleDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.LoginUserDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUserDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.UserQuery;
import edu.guet.studentworkmanagementsystem.entity.vo.user.LoginUserVO;
import edu.guet.studentworkmanagementsystem.entity.vo.user.UserDetailVO;
import edu.guet.studentworkmanagementsystem.service.user.UserService;
import jakarta.annotation.security.PermitAll;
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
    public BaseResponse<LoginUserVO> login(@RequestBody LoginUserDTO loginUserDTO) throws JsonProcessingException {
        return userService.login(loginUserDTO);
    }
    @PreAuthorize("hasAuthority('user:insert') and hasAuthority('user_role:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> addUser(@RequestBody RegisterUserDTO registerUserDTO) {
        return userService.addUser(registerUserDTO);
    }
    @PreAuthorize("hasAuthority('user:insert') and hasAuthority('user_role:insert')")
    @PostMapping("/adds")
    public <T> BaseResponse<T> addUsers(@RequestBody List<RegisterUserDTO> registerUserDTOList) {
        return userService.addUsers(registerUserDTOList);
    }
    @PreAuthorize("hasAuthority('user:select') and hasAuthority('user_role:select')")
    @GetMapping("/detail/{username}")
    public BaseResponse<UserDetailVO> getUserDetails(@PathVariable String username) {
        return userService.getUserDetails(username);
    }
    @PreAuthorize("hasAuthority('user:select') and hasAuthority('user_role:select')")
    @PostMapping("/gets")
    public BaseResponse<Page<UserDetailVO>> gets(@RequestBody UserQuery query) {
        return userService.gets(query);
    }
    @PreAuthorize(
            "hasAuthority('user:update:all') " +
            "and hasAuthority('user_role:insert') " +
            "and hasAuthority('user_role:delete') " +
            "and hasAuthority('role:select')"
    )
    @PutMapping("/update/role")
    public <T> BaseResponse<T> updateUserRole(@RequestBody UserRoleDTO userRoleDTO) {
        return userService.updateUserRole(userRoleDTO);
    }
    @PermitAll
    @DeleteMapping("/logout")
    public <T> BaseResponse<T> logout() {
        return userService.logout();
    }
}
