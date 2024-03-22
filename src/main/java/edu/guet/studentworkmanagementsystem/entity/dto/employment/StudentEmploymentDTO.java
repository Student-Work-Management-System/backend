package edu.guet.studentworkmanagementsystem.entity.dto.employment;

import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentEmploymentDTO implements Serializable {
    private Long studentEmploymentId;
    private String graduationState;
    private Date graduationYear;
    private String whereabouts;
    private String jobNature;
    private String jobIndustry;
    private String jobLocation;
    private String category;
    private String salary;
    private Student student;
}
