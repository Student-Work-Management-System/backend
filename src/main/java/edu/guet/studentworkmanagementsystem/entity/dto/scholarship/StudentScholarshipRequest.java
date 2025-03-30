package edu.guet.studentworkmanagementsystem.entity.dto.scholarship;

import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
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
public class StudentScholarshipRequest implements Serializable {
    @NotBlank(message = "学生奖学金id不能为空", groups = {UpdateGroup.class})
    private String studentScholarshipId;
    @NotBlank(message = "学号不能为空", groups = {InsertGroup.class})
    private String studentId;
    @NotBlank(message = "奖学金id不能为空", groups = {InsertGroup.class})
    private String scholarshipId;
    @Nullable
    @NotBlank(message = "奖学年不能为空", groups = {InsertGroup.class})
    @Pattern(regexp = "^2\\d{3}-2\\d{3}$", message = "请输入正确的获奖学年格式, 例如: 2023-2024", groups = {InsertGroup.class, UpdateGroup.class})
    private String awardYear;
}
