package edu.guet.studentworkmanagementsystem.mapper.authority;

import com.mybatisflex.core.BaseMapper;
import edu.guet.studentworkmanagementsystem.entity.po.user.Permission;
import edu.guet.studentworkmanagementsystem.entity.po.user.RolePermission;
import edu.guet.studentworkmanagementsystem.entity.vo.authority.RolePermissionVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RolePermissionMapper extends BaseMapper<RolePermission> {
    @Select("select * from role " +
            "inner join role_permission on role.rid = role_permission.rid " +
            "inner join permission on role_permission.pid = permission.pid " +
            "where role.rid = ${rid}")
    List<Permission> getRolePermission(String rid);
}
