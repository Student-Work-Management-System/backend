package edu.guet.studentworkmanagementsystem.entity.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentQuery implements Serializable {
    private String search;
    private String gender;
    private String nativePlace;
    private String majorId;
    private String grade;
    private String nation;
    private String politicsStatus;
    private String phone;
    private String email;
    private String classNo;
    private Boolean enabled;
    private Integer pageNo;
    private Integer pageSize;
}
