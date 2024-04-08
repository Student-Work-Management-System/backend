package edu.guet.studentworkmanagementsystem.entity.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTO implements Serializable {
    @NotBlank(message = "工号/学号不能为空")
    private String username;
    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    @NotBlank(message = "邮箱不能为空")
    private String email;
    @NotBlank(message = "密码不能为空")
    private String password;
    private List<String> roles;
}
