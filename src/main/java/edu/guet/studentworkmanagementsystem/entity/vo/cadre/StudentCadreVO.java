package edu.guet.studentworkmanagementsystem.entity.vo.cadre;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCadreVO implements Serializable {
    private String studentCadreId;
    private String studentId;
    private String cadreId;
    private String name;
    private String gender;
    private String majorName;
    private String grade;
    private String cadrePosition;
    private String cadreLevel;
    private String appointmentStartTerm;
    private String appointmentEndTerm;
    private String comment;
}
