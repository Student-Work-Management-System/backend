package edu.guet.studentworkmanagementsystem.entity.vo.other;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserWithCounselorRole {
    private String uid;
    private String username;
    private String realName;
}
