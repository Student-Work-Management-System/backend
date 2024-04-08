package edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertStudentPovertyAssistanceDTO implements Serializable {
    @NotBlank(message = "学号不能为空")
    private String studentId;
    @NotBlank(message = "认证标准id不能为空")
    private String povertyAssistanceId;
    @NotBlank(message = "获资助年份不能为空")
    @Pattern(regexp = "^2\\d{3}-2\\d{3}$", message = "请输入正确格式")
    private String assistanceYear;
}
