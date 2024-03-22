package edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentPovertyAssistanceDTO implements Serializable {
    @NotBlank(message = "学号不能为空")
    private String studentId;
    @NotNull(message = "贫困补助记录id不能为空")
    private Long povertyAssistanceId;
    private String assistanceYear;
}
