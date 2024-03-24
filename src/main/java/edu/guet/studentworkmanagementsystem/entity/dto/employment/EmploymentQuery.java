package edu.guet.studentworkmanagementsystem.entity.dto.employment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentQuery implements Serializable {
    private String majorIn;
    private String grade;
}
