package edu.guet.studentworkmanagementsystem.service.competition.impl;

import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.entity.po.competition.StudentCompetitionTeam;
import edu.guet.studentworkmanagementsystem.entity.vo.competition.TeamItem;
import edu.guet.studentworkmanagementsystem.mapper.competition.CompetitionTeamMapper;
import edu.guet.studentworkmanagementsystem.service.competition.CompetitionTeamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static edu.guet.studentworkmanagementsystem.entity.po.competition.table.StudentCompetitionTeamTableDef.STUDENT_COMPETITION_TEAM;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.DegreeTableDef.DEGREE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.GradeTableDef.GRADE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentBasicTableDef.STUDENT_BASIC;

@Service
public class CompetitionTeamServiceImpl extends ServiceImpl<CompetitionTeamMapper, StudentCompetitionTeam> implements CompetitionTeamService {

    @Override
    @Transactional
    public boolean insertCompetitionTeamBatch(List<StudentCompetitionTeam> competitionTeams) {
        return mapper.insertBatch(competitionTeams) == competitionTeams.size();
    }

    @Override
    public List<TeamItem> getTeamByStudentCompetitionId(String studentCompetitionId) {
        return QueryChain.of(StudentCompetitionTeam.class)
                .select(
                        STUDENT_BASIC.STUDENT_ID,
                        STUDENT_BASIC.NAME,
                        MAJOR.MAJOR_NAME,
                        GRADE.GRADE_NAME,
                        DEGREE.DEGREE_NAME
                )
                .from(STUDENT_COMPETITION_TEAM)
                .innerJoin(STUDENT_BASIC).on(STUDENT_BASIC.STUDENT_ID.eq(STUDENT_COMPETITION_TEAM.STUDENT_ID))
                .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT_BASIC.MAJOR_ID))
                .innerJoin(GRADE).on(GRADE.GRADE_ID.eq(STUDENT_BASIC.GRADE_ID))
                .innerJoin(DEGREE).on(DEGREE.DEGREE_ID.eq(STUDENT_BASIC.DEGREE_ID))
                .where(STUDENT_COMPETITION_TEAM.STUDENT_COMPETITION_ID.eq(studentCompetitionId))
                .listAs(TeamItem.class);
    }

    @Override
    @Transactional
    public boolean deleteTeamByStudentCompetitionId(String studentCompetitionId) {
        return mapper.deleteById(studentCompetitionId) > 0;
    }

}
