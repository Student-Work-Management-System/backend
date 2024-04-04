package edu.guet.studentworkmanagementsystem.entity.dto.scholarship;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank(message = "奖学金id不能为空")
    private String scholarshipId;
    @Pattern(regexp = "^2\\d{3}-2\\d{3}$", message = "请输入正确的获奖学年格式, 例如: 2023-2024")
    private String awardYear;
}
