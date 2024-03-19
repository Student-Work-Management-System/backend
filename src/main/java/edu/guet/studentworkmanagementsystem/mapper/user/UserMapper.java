package edu.guet.studentworkmanagementsystem.mapper.user;


import com.mybatisflex.core.BaseMapper;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import org.apache.ibatis.annotations.Select;


public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where username = #{username};")
    User getUserById(String username);
}
