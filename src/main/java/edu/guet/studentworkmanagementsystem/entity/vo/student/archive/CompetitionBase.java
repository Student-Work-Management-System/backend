package edu.guet.studentworkmanagementsystem.entity.vo.student.archive;

import edu.guet.studentworkmanagementsystem.entity.vo.competition.TeamItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionBase {
    private String studentCompetitionId;
    private String competitionId;
    private String competitionName;
    private String subCompetitionName;
    private String competitionNature;
    private String competitionType;
    private String level;
    private String headerId;
    private String headerName;
    private String evidence;
    private LocalDate date;
    private List<TeamItem> team;
}
