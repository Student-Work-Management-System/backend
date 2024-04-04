package edu.guet.studentworkmanagementsystem.entity.dto.scholarship;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentScholarshipType implements Serializable {
    @NotBlank(message = "学号不能为空")
    private String studentId;
    @NotBlank(message = "原奖学金id不能为空")
    private String oldScholarshipId;
    @NotBlank(message = "新奖学金id不能为空")
    private String newScholarshipId;
}
