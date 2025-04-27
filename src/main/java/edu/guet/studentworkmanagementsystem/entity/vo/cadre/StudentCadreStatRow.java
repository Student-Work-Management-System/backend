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
    private String majorName;
    private String cadreName;
    private String cadreBelong;
    private String appointmentTime;
}
