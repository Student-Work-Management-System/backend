package edu.guet.studentworkmanagementsystem.entity.dto.employment;

import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentEmploymentDTO implements Serializable {
    @NotNull(message = "学生就业id不能为空")
    private Long studentEmploymentId;
    @NotBlank(message = "学号不能为空")
    private String studentId;
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
