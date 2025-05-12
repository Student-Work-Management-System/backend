package edu.guet.studentworkmanagementsystem.entity.vo.competition;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class StudentCompetitionItem implements Serializable {
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
    private String state;
    private String rejectReason;
}
