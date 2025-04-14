package edu.guet.studentworkmanagementsystem.entity.dto.enrollment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentQuery implements Serializable {
    private String search;
    private String politicId;
    private String majorId;
    private String degreeId;
    private Integer pageNo;
    private Integer pageSize;
}
