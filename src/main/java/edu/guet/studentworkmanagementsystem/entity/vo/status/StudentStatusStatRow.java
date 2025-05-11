package edu.guet.studentworkmanagementsystem.entity.vo.status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentStatusStatRow {
    private String gradeName;
    private String majorName;
    private String statusName;
    private String number;
}
