package edu.guet.studentworkmanagementsystem.entity.dto.scholarship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScholarshipQuery implements Serializable {
    private String search;
    private String gradeId;
    private String majorId;
    private String scholarshipLevel;
    private String awardYear;
    private Integer pageNo;
    private Integer pageSize;
}
