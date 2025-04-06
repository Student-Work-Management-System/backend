package edu.guet.studentworkmanagementsystem.entity.vo.scholarship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentScholarshipItem implements Serializable {
    private String studentScholarshipId;
    private String studentId;
    private String name;
    private String gradeName;
    private String majorName;
    private String scholarshipId;
    private String scholarshipName;
    private String scholarshipLevel;
    private String awardYear;
}
