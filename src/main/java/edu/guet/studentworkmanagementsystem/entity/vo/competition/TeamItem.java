package edu.guet.studentworkmanagementsystem.entity.vo.competition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamItem {
    private int studentId;
    private String name;
    private String majorName;
    private String gradeName;
    private String degreeName;
}
