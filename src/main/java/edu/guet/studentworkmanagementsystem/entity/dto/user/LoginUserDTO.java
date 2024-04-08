package edu.guet.studentworkmanagementsystem.entity.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDTO implements Serializable {
    @NotBlank(message = "工号/学号不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
}
