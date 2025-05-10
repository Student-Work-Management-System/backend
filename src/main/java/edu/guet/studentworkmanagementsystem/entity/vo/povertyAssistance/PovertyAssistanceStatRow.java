package edu.guet.studentworkmanagementsystem.entity.vo.povertyAssistance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PovertyAssistanceStatRow {
    private String gradeName;
    private String majorName;
    private String povertyLevel;
    private String number;
}
