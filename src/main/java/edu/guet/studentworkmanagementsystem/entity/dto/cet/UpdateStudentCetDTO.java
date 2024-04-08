package edu.guet.studentworkmanagementsystem.entity.dto.cet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentCetDTO implements Serializable {
    @NotBlank(message = "学生CET考试id不能为空")
    private String studentCetId;
    private String studentId;
    private Long score;
    @Nullable
    @Pattern(regexp = "^CET[4|6]$", message = "类型格式: CET4或CET6")
    @Size(min = 4, max = 4, message = "仅支持单独输入一个类型")
    private String examType;
    @Nullable
    @Pattern(regexp = "^2\\d{3}-2\\d{3}_[1|2]$", message = "日期格式: YYYY-YYYY_[1|2]")
    private String examDate;
    private String certificateNumber;
}
