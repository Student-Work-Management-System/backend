package edu.guet.studentworkmanagementsystem.entity.dto.enrollmentInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentQuery implements Serializable {
    private String from;
    private String enrollCollege;
    private String enrollMajor;
    private String firstMajor;
    private Integer pageNo;
    private Integer pageSize;
}
