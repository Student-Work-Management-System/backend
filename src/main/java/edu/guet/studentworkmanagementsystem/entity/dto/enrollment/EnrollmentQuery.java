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
    private String examineeId;
    private String id;
    private String origin;
    private String enrollMajorId;
    private String firstMajor;
    private String enrollTime;
    private Integer pageNo;
    private Integer pageSize;
}
