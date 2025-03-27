package edu.guet.studentworkmanagementsystem.entity.dto.competition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionQuery {
    private String search;
    private String competitionNature;
    private String competitionType;
    private Integer pageNo;
    private Integer pageSize;
}
