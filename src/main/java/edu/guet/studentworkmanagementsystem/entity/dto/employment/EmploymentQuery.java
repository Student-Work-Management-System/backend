package edu.guet.studentworkmanagementsystem.entity.dto.employment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentQuery implements Serializable {
    private String majorId;
    private String grade;
    private String graduationYear;
    private Integer pageNo;
    private Integer pageSize;
    private String search;
}
