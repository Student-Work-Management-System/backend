package edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentStudentPovertyAssistanceVO implements Serializable {
    private String studentPovertyAssistanceId;
    private String studentId;
    private String povertyAssistanceId;
    private String name;
    private String grade;
    private String majorName;
    private String povertyLevel;
    private String povertyType;
    private String povertyAssistanceStandard;
    private String assistanceYear;
}
