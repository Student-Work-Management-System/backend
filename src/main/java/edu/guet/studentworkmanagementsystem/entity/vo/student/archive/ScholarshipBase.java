package edu.guet.studentworkmanagementsystem.entity.vo.student.archive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScholarshipBase {
    private String scholarshipName;
    private String scholarshipLevel;
    private String awardYear;
}
