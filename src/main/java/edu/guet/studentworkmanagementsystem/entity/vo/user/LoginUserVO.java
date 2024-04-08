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
    private String uid;
    private String username;
    private String realName;
    private String email;
    private List<SystemAuthority> authorities;
    private String token;
    public LoginUserVO(User user, List<SystemAuthority> authorities, String token) {
        this.uid = user.getUid();
        this.username = user.getUsername();
        this.realName = user.getRealName();
        this.email = user.getEmail();
        this.authorities = authorities;
        this.token = token;
    }
}
