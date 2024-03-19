package edu.guet.studentworkmanagementsystem.entity.dto.user;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RegisterUserDTO implements Serializable {
    private String username;
    private String realName;
    private String phone;
    private String password;
    private List<String> roles;
}
