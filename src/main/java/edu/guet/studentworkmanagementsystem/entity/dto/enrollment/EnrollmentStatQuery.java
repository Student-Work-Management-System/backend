package edu.guet.studentworkmanagementsystem.entity.dto.enrollment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentStatQuery implements Serializable {
    private String gradeId;
    private String majorId;
}
