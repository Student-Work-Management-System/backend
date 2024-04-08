package edu.guet.studentworkmanagementsystem.entity.dto.schoolPrecaution;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentSchoolPrecautionDTO implements Serializable {
    @NotBlank(message = "学生学业预警id不能为空")
    private String studentSchoolPrecautionId;
    private String studentId;
    /**
     * 学业预警等级
     */
    private String schoolPrecautionLevel;
    /**
     * YYYY-YYYY_term
     */
    @Nullable
    @Pattern(regexp = "^2\\d{3}-2\\d{3}_[1|2]$", message = "请输入正确的学业预警学期格式, 例如: 2023-2024_1")
    private String precautionTerm;
    /**
     * 原因
     */
    private String detailReason;
    /**
     * 备注
     */
    private String comment;
}
