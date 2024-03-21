package edu.guet.studentworkmanagementsystem.entity.dto.student;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO implements Serializable {
    @NotBlank(message = "学号不能为空")
    private String studentId;
    private String gender;
    private String nativePlace;
    private String postalCode;
    private String phone;
    private String nation;
    private String collegeName;
    private String majorIn;
    private String grade;
    private String classNo;
    private String politicsStatus;
}
