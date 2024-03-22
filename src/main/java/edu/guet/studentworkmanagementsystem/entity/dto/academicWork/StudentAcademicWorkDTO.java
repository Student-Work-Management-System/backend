package edu.guet.studentworkmanagementsystem.entity.dto.academicWork;

import edu.guet.studentworkmanagementsystem.entity.po.academicWork.AcademicWork;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAcademicWorkDTO implements Serializable {
    @NotBlank(message = "学号不能为空")
    private String studentId;
    private String academicWorkName;
    private String academicWorkType;
    private Long additionalInfoId;
    private String authors;
    private String evidence;
    private AcademicWork academicWork;
}
