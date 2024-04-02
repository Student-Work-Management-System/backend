package edu.guet.studentworkmanagementsystem.entity.po.user;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id(keyType = KeyType.Auto)
    private String rid;
    private String roleName;
    private String roleDesc;
    public Role(RoleDTO roleDTO) {
        this.rid = roleDTO.getRid();
        this.roleName = roleDTO.getRoleName();
        this.roleDesc = roleDTO.getRoleDesc();
    }
}
