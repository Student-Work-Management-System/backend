package edu.guet.studentworkmanagementsystem.entity.dto.authority;

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
public class UserRoleRequest implements Serializable {
    @NotBlank(message = "用户id不能为空")
    private String uid;
    private Set<String> roles;
}
