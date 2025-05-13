package edu.guet.studentworkmanagementsystem.entity.vo.user;

import edu.guet.studentworkmanagementsystem.entity.po.user.Role;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentArchive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserArchive {
    private String username;
    private String realName;
    private String email;
    private String phone;
    private List<Role> roles;
    private StudentArchive studentArchive;
}
