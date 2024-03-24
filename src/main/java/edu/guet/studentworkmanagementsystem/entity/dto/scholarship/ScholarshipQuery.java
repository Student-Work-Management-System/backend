package edu.guet.studentworkmanagementsystem.entity.dto.scholarship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScholarshipQuery implements Serializable {
    private String grade;
    private String majorIn;
    private String awardYear;
}
