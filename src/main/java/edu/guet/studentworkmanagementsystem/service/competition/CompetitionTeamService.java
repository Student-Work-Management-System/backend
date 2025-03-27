package edu.guet.studentworkmanagementsystem.service.competition;

import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.entity.po.competition.StudentCompetitionTeam;
import edu.guet.studentworkmanagementsystem.entity.vo.competition.TeamItem;

import java.util.List;

public interface CompetitionTeamService extends IService<StudentCompetitionTeam> {
    boolean insertCompetitionTeamBatch(List<StudentCompetitionTeam> competitionTeams);
    List<TeamItem> getTeamByStudentCompetitionId(String studentCompetitionId);
    boolean deleteTeamByStudentCompetitionId(String studentCompetitionId);
}
