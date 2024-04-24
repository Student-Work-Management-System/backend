package edu.guet.studentworkmanagementsystem.entity.dto.cadre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentCadreDTO {
    @NotBlank(message = "学生任职id不能为空")
    private String studentCadreId;
    private String studentId;
    private String cadreId;
    @Nullable
    @Pattern(regexp = "^2\\d{3}-2\\d{3}_(1|2)$\n", message = "任职开始学期不能为空")
    private String appointmentStartTerm;
    @Nullable
    @Pattern(regexp = "^2\\d{3}-2\\d{3}_(1|2)$\n", message = "任职结束学期不能为空")
    private String appointmentEndTerm;
    private String comment;
}
