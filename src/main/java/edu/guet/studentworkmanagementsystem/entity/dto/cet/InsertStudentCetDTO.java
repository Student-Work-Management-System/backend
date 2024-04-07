package edu.guet.studentworkmanagementsystem.entity.dto.cet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertStudentCetDTO implements Serializable {
    @NotBlank(message = "学号不能为空")
    private String studentId;
    @NotNull(message = "分数不能为空")
    private Long score;
    @NotBlank(message = "考试日期不能为空")
    private String examDate;
    @NotBlank(message = "考试证书不能为空")
    private String certificateNumber;
    @NotBlank(message = "考试类型不能为空")
    @Pattern(regexp = "^CET[4|6]$", message = "类型: CET4或CET6")
    @Size(min = 4, max = 4, message = "仅支持单独输入一个类型")
    private String examType;
}
