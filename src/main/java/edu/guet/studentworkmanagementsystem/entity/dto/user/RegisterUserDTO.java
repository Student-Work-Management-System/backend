package edu.guet.studentworkmanagementsystem.entity.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTO implements Serializable {
    private String username;
    private String realName;
    private String phone;
    private String password;
    private List<String> roles;
}
