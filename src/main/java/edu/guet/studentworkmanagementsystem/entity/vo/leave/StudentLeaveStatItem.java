package edu.guet.studentworkmanagementsystem.entity.vo.leave;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentLeaveStatItem {
    private String gradeName;
    private String majorName;
    private String type;
    private boolean internship;
    private boolean destroyed;
}
