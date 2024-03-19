package edu.guet.studentworkmanagementsystem.entity.po.user;


import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Table("user_role")
@Data
@AllArgsConstructor
public class UserRole {
    @Id
    private String uid;
    @Id
    private String rid;
}
