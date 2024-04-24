package edu.guet.studentworkmanagementsystem.entity.dto.employment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentEmploymentDTO implements Serializable {
    @NotBlank(message = "学生就业信息id不能为空")
    private String studentEmploymentId;
    private String graduationState;
    private String graduationYear;
    private String whereabouts;
    private String jobNature;
    private String jobIndustry;
    private String jobLocation;
    private String category;
    private String salary;
}
