package edu.guet.studentworkmanagementsystem.entity.dto.cadre;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCadreDTO implements Serializable {
    @NotBlank(message = "学号不能为空")
    private String studentId;
    @NotBlank(message = "职位id不能为空")
    private Long cadreId;
    /**
     * 任职开始学期
     */
    private String appointmentStartTerm;
    /**
     * 任职结束学期
     */
    private String appointmentEndTerm;
    /**
     * 备注
     */
    private String comment;
}
