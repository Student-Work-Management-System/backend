package edu.guet.studentworkmanagementsystem.entity.vo.student.archive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PovertyAssistanceBase {
    private String povertyLevel;
    private String povertyType;
    private String povertyAssistanceStandard;
    private String assistanceYear;
}
