package edu.guet.studentworkmanagementsystem.mapper.authority;

import com.mybatisflex.core.BaseMapper;
import edu.guet.studentworkmanagementsystem.entity.po.user.Role;
import edu.guet.studentworkmanagementsystem.entity.po.user.UserRole;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserRoleMapper extends BaseMapper<UserRole> {
    @Select("select role.* from swms.user " +
            "left join swms.user_role on user.uid = user_role.uid " +
            "left join swms.role on user_role.rid = role.rid " +
            "where swms.user.uid = #{uid}")
    List<Role> getUserRole(String uid);
}
