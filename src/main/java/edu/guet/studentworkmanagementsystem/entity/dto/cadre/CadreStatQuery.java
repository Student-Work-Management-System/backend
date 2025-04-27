package edu.guet.studentworkmanagementsystem.entity.dto.cadre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CadreStatQuery {
    private String gradeId;
    private String majorId;
    private String cadreLevel;
    private String startTerm;
    private String endTerm;
}
