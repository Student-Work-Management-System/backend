package edu.guet.studentworkmanagementsystem.entity.dto.enrollment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentQuery implements Serializable {
    private String search;
    private Integer pageNo;
    private Integer pageSize;
}
