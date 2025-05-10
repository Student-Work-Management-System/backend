package edu.guet.studentworkmanagementsystem.entity.dto.competition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionStatQuery {
    private String gradeId;
    private String majorId;
}
