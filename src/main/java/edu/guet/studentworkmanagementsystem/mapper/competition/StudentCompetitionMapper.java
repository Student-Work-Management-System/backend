package edu.guet.studentworkmanagementsystem.mapper.competition;

import com.mybatisflex.core.BaseMapper;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.CompetitionStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.competition.StudentCompetition;
import edu.guet.studentworkmanagementsystem.entity.vo.competition.CompetitionStatRow;

import java.util.List;

public interface StudentCompetitionMapper extends BaseMapper<StudentCompetition> {
    List<CompetitionStatRow> getStat(CompetitionStatQuery query);
}
