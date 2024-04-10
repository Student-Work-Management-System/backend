package edu.guet.studentworkmanagementsystem.entity.vo.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentVO implements Serializable {
    private String studentId;
    private String idNumber;
    private String name;
    private String gender;
    private String nativePlace;
    private String postalCode;
    private String phone;
    private String nation;
    private String majorId;
    private String majorName;
    private String grade;
    private String classNo;
    private String politicsStatus;
}
