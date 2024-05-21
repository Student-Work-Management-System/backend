package edu.guet.studentworkmanagementsystem.entity.dto.authority;

import edu.guet.studentworkmanagementsystem.entity.po.user.Permission;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO implements Serializable {
    @NotBlank(message = "角色名不能为空")
    private String roleName;
    @NotBlank(message = "角色描述不能为空")
    private String roleDesc;
    private List<Permission> permissions;
}
