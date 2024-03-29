package edu.guet.studentworkmanagementsystem.entity.dto.scholarship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScholarshipQuery implements Serializable {
    private String grade;
    private String majorId;
    private String awardYear;
    private Integer pageNo;
    private Integer pageSize;
}
