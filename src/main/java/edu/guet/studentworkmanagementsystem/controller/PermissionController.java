package edu.guet.studentworkmanagementsystem.controller;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.RoleRequest;
import edu.guet.studentworkmanagementsystem.entity.dto.authority.RolePermissionRequest;
import edu.guet.studentworkmanagementsystem.entity.po.user.Permission;
import edu.guet.studentworkmanagementsystem.entity.vo.authority.PermissionTreeItem;
import edu.guet.studentworkmanagementsystem.entity.vo.authority.RolePermissionDetail;
import edu.guet.studentworkmanagementsystem.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class PermissionController {
    @Autowired
    private UserService userService;
    @PreAuthorize(
            "hasAuthority('role:select') " +
            "and hasAuthority('role_permission:select')"
    )
    @GetMapping("/gets")
    public BaseResponse<List<RolePermissionDetail>> getAllRole() {
        return userService.getAllRole();
    }
    @PreAuthorize("hasAuthority('permission:select')")
    @GetMapping("/permission/gets")
    public BaseResponse<List<PermissionTreeItem>> getPermissionTree() {
        return userService.getPermissionTree();
    }
    @PreAuthorize(
            "hasAuthority('role_permission:insert') " +
            "and hasAuthority('role_permission:delete') " +
            "and hasAuthority('permission:select')"
    )
    @PutMapping("/update/role/permission")
    public <T> BaseResponse<T> updateRolePermission(@RequestBody @Valid RolePermissionRequest rolePermissionRequest) {
        return userService.updateRolePermission(rolePermissionRequest);
    }
    @PreAuthorize("hasAuthority('role:insert') and hasAuthority('role_permission:insert')")
    @PostMapping("/add/role")
    public <T> BaseResponse<T> addRole(@RequestBody @Valid RoleRequest roleItem) {
        return userService.addRole(roleItem);
    }
    @PreAuthorize("hasAuthority('permission:insert')")
    @PostMapping("/add/permission")
    public  <T> BaseResponse<T> addPermission(@RequestBody Permission permission) {
        return userService.addPermission(permission);
    }
    @PreAuthorize(
            "hasAuthority('role:delete') " +
            "and hasAuthority('user_role:delete') " +
            "and hasAuthority('role_permission:delete') "
    )
    @DeleteMapping("/delete/role/{rid}")
    public <T> BaseResponse<T> deleteRole(@PathVariable String rid) {
        return userService.deleteRole(rid);
    }
    @PreAuthorize(
            "hasAuthority('permission:delete') " +
            "and hasAuthority('role_permission:delete')"
    )
    @DeleteMapping("/delete/permission/{pid}")
    public <T> BaseResponse<T> deletePermission(@PathVariable String pid) {
        return userService.deletePermission(pid);
    }
}
