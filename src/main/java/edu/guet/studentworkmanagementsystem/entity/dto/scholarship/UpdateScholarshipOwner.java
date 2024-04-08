package edu.guet.studentworkmanagementsystem.entity.dto.scholarship;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateScholarshipOwner implements Serializable {
    @NotBlank(message = "原奖学金获得者学号不能为空")
    private String oldStudentId;
    @NotBlank(message = "新奖学金获得者学号不能为空")
    private String newStudentId;
    @NotBlank(message = "奖学金id不能为空")
    private String scholarshipId;
}
