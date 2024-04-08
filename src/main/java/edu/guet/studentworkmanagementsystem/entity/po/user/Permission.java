package edu.guet.studentworkmanagementsystem.entity.po.user;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission{
    @Id(keyType = KeyType.Auto)
    private String pid;
    private String permissionName;
    private String permissionDesc;
}
