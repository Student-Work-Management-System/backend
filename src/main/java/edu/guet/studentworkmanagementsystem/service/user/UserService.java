package edu.guet.studentworkmanagementsystem.service.user;

import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.entity.dto.user.LoginUserDTO;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.entity.vo.user.UserVO;

public interface UserService extends IService<User> {
    /**
     * 要求使用SpringSecurity的UserDetailsService进行验证并存入
     * @param loginUserDTO 学生账号使用学号、工作人员账号使用工号<br/>学生密码默认使用身份证后六位, 工作人员用户由管理员创建
     * @return 包含用户信息、权限和token的用户类
     */
    UserVO login(LoginUserDTO loginUserDTO);
}
