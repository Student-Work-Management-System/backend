package edu.guet.studentworkmanagementsystem.entity.dto.authority;

import edu.guet.studentworkmanagementsystem.entity.po.user.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO implements Serializable {
    private String rid;
    private String roleName;
    private String roleDesc;
    private List<Permission> permissions;
}
