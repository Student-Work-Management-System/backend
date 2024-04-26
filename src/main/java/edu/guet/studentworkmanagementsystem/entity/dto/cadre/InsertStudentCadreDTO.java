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
public class InsertStudentCadreDTO {
    @NotBlank(message = "学号不能为空")
    private String studentId;
    @NotBlank(message = "职位id不能为空")
    private String cadreId;
    @Pattern(regexp = "^2\\d{3}-2\\d{3}_([12])$", message = "任职开始学期不能为空")
    private String appointmentStartTerm;
    @Pattern(regexp = "^2\\d{3}-2\\d{3}_([12])$", message = "任职结束学期不能为空")
    private String appointmentEndTerm;
    private String comment;
}
