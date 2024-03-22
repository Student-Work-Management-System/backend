package edu.guet.studentworkmanagementsystem.entity.dto.scholarship;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentScholarshipDTO implements Serializable {
    @NotBlank(message = "学号不能为空")
    private String studentId;
    @NotNull(message = "奖学金id不能为空")
    private Long scholarshipId;
    private String awardYear;
}
