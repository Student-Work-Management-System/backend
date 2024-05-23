package edu.guet.studentworkmanagementsystem.entity.dto.employment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentQuery implements Serializable {
    private String search;
    private String majorId;
    private String grade;
    private String graduationYear;
    private Boolean enabled;
    private Integer pageNo;
    private Integer pageSize;
}
