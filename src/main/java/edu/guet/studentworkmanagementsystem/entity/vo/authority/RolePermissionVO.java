package edu.guet.studentworkmanagementsystem.entity.vo.authority;

import edu.guet.studentworkmanagementsystem.entity.po.user.Permission;
import edu.guet.studentworkmanagementsystem.entity.po.user.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionVO {
    private String rid;
    private String roleName;
    private String roleDesc;
    private List<Permission> permissionList;
    public RolePermissionVO(Role role, List<Permission> permissions) {
        this.rid = role.getRid();
        this.roleName = role.getRoleName();
        this.roleDesc = role.getRoleDesc();
        this.permissionList = permissions;
    }
}
