package edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance;

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
public class UpdateStudentPovertyAssistanceDTO implements Serializable {
    @NotBlank(message = "学生贫困补助id不能为空")
    private String studentPovertyAssistanceId;
    private String studentId;
    private String povertyAssistanceId;
    @Nullable
    @Pattern(regexp = "^2\\d{3}-2\\d{3}$")
    private String assistanceYear;
}
