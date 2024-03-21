package edu.guet.studentworkmanagementsystem.entity.dto.authority;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDTO implements Serializable {
    @NotBlank(message = "用户id不能为空")
    private String uid;
    private Set<String> roles;
}
