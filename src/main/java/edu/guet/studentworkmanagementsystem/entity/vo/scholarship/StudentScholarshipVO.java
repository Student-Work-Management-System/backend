package edu.guet.studentworkmanagementsystem.entity.vo.scholarship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentScholarshipVO implements Serializable {
    private String studentId;
    private String name;
    private String scholarshipName;
    private String scholarshipLevel;
    private String awardYear;
}
