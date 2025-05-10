package edu.guet.studentworkmanagementsystem.entity.dto.employment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentStatQuery implements Serializable {
    private String gradeId;
    private String majorId;
}
