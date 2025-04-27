package edu.guet.studentworkmanagementsystem.entity.vo.cadre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentCadreStatRow {
    private String gradeName;
    private String majorName;
    private String cadreLevel;
    private String cadreBelong;
    private String total;
    private String appointmentTime;
}
