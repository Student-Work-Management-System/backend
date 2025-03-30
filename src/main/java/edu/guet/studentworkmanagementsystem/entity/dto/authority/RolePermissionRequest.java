package edu.guet.studentworkmanagementsystem.entity.dto.authority;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionRequest implements Serializable {
    @NotBlank(message = "角色id不能为空")
    private String rid;
    private Set<String> permissions;
}
