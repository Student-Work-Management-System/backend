package edu.guet.studentworkmanagementsystem.entity.dto.cet;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCetDTO implements Serializable {
    @NotBlank(message = "studentCETId不能为空")
    private Long studentCETId;
    @NotBlank(message = "studentCETId不能为空")
    private String studentId;
    private Long score;
    private String examType;
    private String examDate;
    private String certificateNumber;
}
