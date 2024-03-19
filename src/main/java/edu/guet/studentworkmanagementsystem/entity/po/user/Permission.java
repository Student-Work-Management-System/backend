package edu.guet.studentworkmanagementsystem.entity.po.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Table("permission")
@Data
@AllArgsConstructor
public class Permission{
    @Id(keyType = KeyType.Auto)
    @JsonIgnore
    private String pid;
    private String permissionName;
    private String permissionDescription;
}
