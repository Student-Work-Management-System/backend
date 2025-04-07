package edu.guet.studentworkmanagementsystem.entity.dto.employment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentQuery implements Serializable {
    private String search;
    private String gender;
    private String majorId;
    private String gradeId;
    private String degreeId;
    private String politicId;
    private String graduationYear;
    private Integer pageNo;
    private Integer pageSize;
}
