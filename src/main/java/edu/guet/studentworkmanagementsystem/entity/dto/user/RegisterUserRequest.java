package edu.guet.studentworkmanagementsystem.entity.dto.user;

import edu.guet.studentworkmanagementsystem.common.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest implements Serializable {
    @NotBlank(message = "工号/学号不能为空")
    private String username;
    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    @Email(message = "请输入正确的邮箱")
    private String email;
    @NotBlank(message = "密码不能为空")
    private String password;
    @Phone
    @NotBlank(message = "手机号不能为空")
    private String phone;
    private Set<String> roles;
}
