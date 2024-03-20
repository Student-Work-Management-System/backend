package edu.guet.studentworkmanagementsystem.entity.vo.user;

import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.securiy.SystemAuthority;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class LoginUserVO implements Serializable {
    private User user;
    private List<SystemAuthority> authorities;
    private String token;
}
