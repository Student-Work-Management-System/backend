package edu.guet.studentworkmanagementsystem.entity.vo.authority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionTreeVO{
    private String pid;
    private String permissionName;
    private String permissionDesc;
    private List<PermissionTreeVO> children;
}
