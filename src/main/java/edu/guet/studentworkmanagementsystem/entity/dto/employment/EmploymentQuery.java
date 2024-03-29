package edu.guet.studentworkmanagementsystem.entity.dto.employment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentQuery implements Serializable {
    private String majorId;
    private String grade;
    private Integer pageNo;
    private Integer pageSize;
}
