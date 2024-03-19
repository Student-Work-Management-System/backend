package edu.guet.studentworkmanagementsystem.controller;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.user.LoginUserDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUserDTO;
import edu.guet.studentworkmanagementsystem.entity.vo.user.UserVO;
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
    public BaseResponse<UserVO> login(@RequestBody LoginUserDTO loginUserDTO) {
        return userService.login(loginUserDTO);
    }
    @PreAuthorize("hasAuthority('user:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> addUser(@RequestBody RegisterUserDTO registerUserDTO) {
        return userService.addUser(registerUserDTO);
    }
    @PreAuthorize("hasAuthority('user:insert')")
    @PostMapping("/adds")
    public <T> BaseResponse<T> addUsers(@RequestBody List<RegisterUserDTO> registerUserDTOList) {
        return userService.addUsers(registerUserDTOList);
    }
}
