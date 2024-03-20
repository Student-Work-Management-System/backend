package edu.guet.studentworkmanagementsystem.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.user.LoginUserDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUserDTO;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.entity.vo.user.UserVO;

import java.util.List;

public interface UserService extends IService<User> {
    BaseResponse<UserVO> login(LoginUserDTO loginUserDTO) throws JsonProcessingException;
    <T> BaseResponse<T> addUser(RegisterUserDTO registerUserDTO);
    <T> BaseResponse<T> addUsers(List<RegisterUserDTO> registerUserDTOList);
}
