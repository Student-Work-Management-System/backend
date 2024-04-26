package edu.guet.studentworkmanagementsystem.entity.dto.enrollment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentQuery implements Serializable {
    private String name;
    private String origin;
    private String enrollSchool;
    private String enrollMajor;
    private String firstMajor;
    private Integer pageNo;
    private Integer pageSize;
}
