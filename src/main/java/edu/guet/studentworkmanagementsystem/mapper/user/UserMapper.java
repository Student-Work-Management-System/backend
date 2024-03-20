package edu.guet.studentworkmanagementsystem.mapper.user;


import com.mybatisflex.core.BaseMapper;
import edu.guet.studentworkmanagementsystem.entity.po.user.Permission;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where username = #{username};")
    User getUserByUsername(String username);
    @Select("select * from swms.user " +
            "left join swms.user_role on user.uid = user_role.uid " +
            "left join swms.role on user_role.rid = role.rid " +
            "inner join swms.role_permission on role.rid = role_permission.rid " +
            "inner join swms.permission on role_permission.pid = permission.pid " +
            "where swms.user.uid = #{uid}")
    List<Permission> getUserPermission(String uid);
}
