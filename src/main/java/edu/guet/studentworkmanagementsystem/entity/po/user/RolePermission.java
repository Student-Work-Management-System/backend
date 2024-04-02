package edu.guet.studentworkmanagementsystem.entity.po.user;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("role_permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePermission {
    @Id
    private String rid;
    @Id
    private String pid;
}
