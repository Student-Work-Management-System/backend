package edu.guet.studentworkmanagementsystem.entity.vo.povertyAssistance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentPovertyAssistanceItem implements Serializable {
    private String studentPovertyAssistanceId;
    private String studentId;
    private String name;
    private String gradeName;
    private String majorName;
    private String povertyAssistanceId;
    private String povertyLevel;
    private String povertyType;
    private String povertyAssistanceStandard;
    private String assistanceYear;
}
