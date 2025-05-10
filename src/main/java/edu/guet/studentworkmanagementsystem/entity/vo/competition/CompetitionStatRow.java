package edu.guet.studentworkmanagementsystem.entity.vo.competition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionStatRow {
    private String gradeName;
    private String majorName;
    private String competitionName;

}
