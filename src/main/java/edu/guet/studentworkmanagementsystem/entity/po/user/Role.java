package edu.guet.studentworkmanagementsystem.entity.po.user;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Table("role")
@Data
@AllArgsConstructor
public class Role {
    @Id(keyType = KeyType.Auto)
    private String rid;
    private String roleName;
    private String roleDescription;
}
