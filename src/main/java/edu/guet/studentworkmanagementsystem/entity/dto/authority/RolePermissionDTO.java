package edu.guet.studentworkmanagementsystem.entity.dto.authority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionDTO implements Serializable {
    private String rid;
    private Set<String> permissions;
}
