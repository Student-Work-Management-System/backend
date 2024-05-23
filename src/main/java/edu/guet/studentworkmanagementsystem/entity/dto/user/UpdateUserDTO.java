package edu.guet.studentworkmanagementsystem.entity.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO implements Serializable {
    @NotBlank(message = "uid不能为空")
    private String uid;
    private String realName;
    private String email;
    private String password;
    private Boolean enabled;
}
