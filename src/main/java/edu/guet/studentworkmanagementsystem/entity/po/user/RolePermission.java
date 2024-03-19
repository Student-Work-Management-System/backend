package edu.guet.studentworkmanagementsystem.entity.po.user;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Table("role_permission")
@Data
@AllArgsConstructor
public class RolePermission {
    @Id
    private String rid;
    @Id
    private String pid;
}
