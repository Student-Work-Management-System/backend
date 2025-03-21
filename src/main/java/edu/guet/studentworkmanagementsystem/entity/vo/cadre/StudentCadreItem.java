package edu.guet.studentworkmanagementsystem.entity.vo.cadre;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCadreItem implements Serializable {
    private String studentCadreId;
    private String studentId;
    private String cadreName;
    private String name;
    private String gender;
    private String majorName;
    private String gradeName;
    private String cadreId;
    private String cadrePosition;
    private String cadreLevel;
    private String cadreBelong;
    private String appointmentStartTerm;
    private String appointmentEndTerm;
    private String comment;
}
