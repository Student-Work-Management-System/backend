package edu.guet.studentworkmanagementsystem.entity.vo.employment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentEmploymentStatRow {
    private String gradeName;
    private String majorName;
}
