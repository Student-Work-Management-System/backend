package edu.guet.studentworkmanagementsystem.entity.vo.user;

import edu.guet.studentworkmanagementsystem.entity.po.other.Degree;
import edu.guet.studentworkmanagementsystem.entity.po.other.Grade;
import edu.guet.studentworkmanagementsystem.securiy.SystemAuthority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDetail implements Serializable {
    private String uid;
    private String username;
    private String realName;
    private String email;
    private List<Grade> chargeGrades;
    private List<Degree> chargeDegrees;
    private List<SystemAuthority> authorities;
    private String token;
}
