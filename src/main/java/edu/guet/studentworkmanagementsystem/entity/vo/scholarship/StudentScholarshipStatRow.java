package edu.guet.studentworkmanagementsystem.entity.vo.scholarship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentScholarshipStatRow {
    private String gradeName;
    private String majorName;
    private String scholarshipName;
    private String scholarshipLevel;
    private String total;
}
