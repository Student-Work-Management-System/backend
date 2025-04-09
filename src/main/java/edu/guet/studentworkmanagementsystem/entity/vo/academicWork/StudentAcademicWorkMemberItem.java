package edu.guet.studentworkmanagementsystem.entity.vo.academicWork;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentAcademicWorkMemberItem {
    private String memberOrder;
    private String uid;
    private Boolean isStudent;
    private String username;
    private String realName;
    private String majorName;
    private String gradeName;
    private String degreeName;
}
