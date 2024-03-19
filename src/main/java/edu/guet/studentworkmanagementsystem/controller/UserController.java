package edu.guet.studentworkmanagementsystem.controller;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.user.LoginUserDTO;
import edu.guet.studentworkmanagementsystem.entity.vo.user.UserVO;
import edu.guet.studentworkmanagementsystem.service.user.UserService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PermitAll
    @PostMapping("/login")
    public BaseResponse<UserVO> login(@RequestBody LoginUserDTO loginUserDTO) {
        return ResponseUtil.success(userService.login(loginUserDTO));
    }
}
