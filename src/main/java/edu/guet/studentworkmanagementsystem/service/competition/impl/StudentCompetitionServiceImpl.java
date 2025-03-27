package edu.guet.studentworkmanagementsystem.service.competition.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.StudentCompetitionQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.StudentCompetitionWithMember;
import edu.guet.studentworkmanagementsystem.entity.po.competition.StudentCompetition;
import edu.guet.studentworkmanagementsystem.entity.po.competition.StudentCompetitionAudit;
import edu.guet.studentworkmanagementsystem.entity.po.competition.StudentCompetitionTeam;
import edu.guet.studentworkmanagementsystem.entity.vo.competition.StudentCompetitionItem;
import edu.guet.studentworkmanagementsystem.entity.vo.competition.TeamItem;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.competition.StudentCompetitionMapper;
import edu.guet.studentworkmanagementsystem.service.competition.CompetitionAuditService;
import edu.guet.studentworkmanagementsystem.service.competition.CompetitionService;
import edu.guet.studentworkmanagementsystem.service.competition.CompetitionTeamService;
import edu.guet.studentworkmanagementsystem.service.competition.StudentCompetitionService;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import edu.guet.studentworkmanagementsystem.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static edu.guet.studentworkmanagementsystem.entity.po.competition.table.CompetitionTableDef.COMPETITION;
import static edu.guet.studentworkmanagementsystem.entity.po.competition.table.StudentCompetitionAuditTableDef.STUDENT_COMPETITION_AUDIT;
import static edu.guet.studentworkmanagementsystem.entity.po.competition.table.StudentCompetitionTableDef.STUDENT_COMPETITION;
import static edu.guet.studentworkmanagementsystem.entity.po.competition.table.StudentCompetitionTeamTableDef.STUDENT_COMPETITION_TEAM;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.DegreeTableDef.DEGREE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.GradeTableDef.GRADE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentBasicTableDef.STUDENT_BASIC;

@Service
public class StudentCompetitionServiceImpl extends ServiceImpl<StudentCompetitionMapper, StudentCompetition> implements StudentCompetitionService {
    @Autowired
    private CompetitionService competitionService;
    @Autowired
    private CompetitionAuditService competitionAuditService;
    @Autowired
    private CompetitionTeamService competitionTeamService;
    @Qualifier("readThreadPool")
    @Autowired
    private ThreadPoolTaskExecutor readThreadPool;

    @Override
    @Transactional
    public <T> BaseResponse<T> insertStudentCompetition(StudentCompetitionWithMember studentCompetitionWithMember) {
        // 先将数据插入student_competition表中, 获取返回的studentCompetitionId
        StudentCompetition studentCompetition = createStudentCompetition(studentCompetitionWithMember);
        int i = mapper.insert(studentCompetition);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        // 检查团队性质, 0: 团队, 1: 单人
        if (competitionService.competitionNatureIsSolo(studentCompetition.getCompetitionId()))
            return ResponseUtil.success();
        // 到此表示为团队竞赛, 需要插入相关表记录队员
        String studentCompetitionId = studentCompetition.getStudentCompetitionId();
        List<StudentCompetitionTeam> team = createTeam(studentCompetitionId, studentCompetitionWithMember.getStudentIds());
        boolean insertCompetitionTeamBatchSuccess = competitionTeamService.insertCompetitionTeamBatch(team);
        if (!insertCompetitionTeamBatchSuccess)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        // 加入审核表
        StudentCompetitionAudit studentCompetitionAudit = createStudentCompetitionAudit(studentCompetitionId);
        boolean insertCompetitionAuditSuccess = competitionAuditService.insertCompetitionAudit(studentCompetitionAudit);
        if (!insertCompetitionAuditSuccess)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    public StudentCompetition createStudentCompetition(StudentCompetitionWithMember studentCompetitionWithMember) {
        return StudentCompetition.builder()
                .competitionId(studentCompetitionWithMember.getCompetitionId())
                .headerId(studentCompetitionWithMember.getHeaderId())
                .level(studentCompetitionWithMember.getLevel())
                .date(studentCompetitionWithMember.getDate())
                .build();
    }

    public List<StudentCompetitionTeam> createTeam(String studentCompetitionId, List<String> studentIds) {
        ArrayList<StudentCompetitionTeam> team = new ArrayList<>();
        studentIds.forEach(studentId -> {
            StudentCompetitionTeam studentCompetitionTeam = new StudentCompetitionTeam(studentCompetitionId, studentId);
            team.add(studentCompetitionTeam);
        });
        return team;
    }

    public StudentCompetitionAudit createStudentCompetitionAudit(String studentCompetitionId) {
        return StudentCompetitionAudit.builder()
                .studentCompetitionId(studentCompetitionId)
                .operatorTime(LocalDate.now())
                .build();
    }

    @Override
    public BaseResponse<List<StudentCompetitionItem>> getOwnStudentCompetition() {
        String studentId = SecurityUtil.getUserCredentials().getUsername();
        CompletableFuture.supplyAsync(() -> {
            List<StudentCompetitionItem> items = QueryChain.of(StudentCompetition.class)
                    .select(
                            COMPETITION.ALL_COLUMNS,
                            STUDENT_COMPETITION.ALL_COLUMNS,
                            STUDENT_BASIC.NAME.as("headerName"),
                            STUDENT_COMPETITION_AUDIT.STATE,
                            STUDENT_COMPETITION_AUDIT.REJECT_REASON
                    )
                    .from(STUDENT_COMPETITION)
                    .innerJoin(COMPETITION).on(COMPETITION.COMPETITION_ID.eq(STUDENT_COMPETITION.COMPETITION_ID))
                    .innerJoin(STUDENT_BASIC).on(STUDENT_BASIC.STUDENT_ID.eq(STUDENT_COMPETITION.HEADER_ID))
                    .innerJoin(STUDENT_COMPETITION_AUDIT).on(STUDENT_COMPETITION_AUDIT.STUDENT_COMPETITION_ID.eq(STUDENT_COMPETITION.STUDENT_COMPETITION_ID))
                    .innerJoin(STUDENT_COMPETITION_TEAM).on(STUDENT_COMPETITION_TEAM.STUDENT_COMPETITION_ID.eq(STUDENT_COMPETITION.STUDENT_COMPETITION_ID))
                    // 队长或队员存在该学号成员即可
                    .where(
                            STUDENT_COMPETITION.HEADER_ID.eq(studentId)
                                    .or(STUDENT_COMPETITION_TEAM.STUDENT_ID.eq(studentId))
                    )
                    .listAs(StudentCompetitionItem.class);
            for (StudentCompetitionItem item : items) {
                String competitionId = item.getCompetitionId();
                if (competitionService.competitionNatureIsSolo(competitionId))
                    continue;
                String studentCompetitionId = item.getStudentCompetitionId();
                List<TeamItem> team = competitionTeamService.getTeamByStudentCompetitionId(studentCompetitionId);
                item.setTeam(team);
            }
            return items;
        }, readThreadPool);
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudentCompetition(String studentCompetitionId) {
        boolean deleteCompetitionAuditSuccess = competitionAuditService.deleteCompetitionAudit(studentCompetitionId);
        if (!deleteCompetitionAuditSuccess)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        boolean deleteTeamSuccess = competitionTeamService.deleteTeamByStudentCompetitionId(studentCompetitionId);
        if (!deleteTeamSuccess)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        int i = mapper.deleteById(studentCompetitionId);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    public BaseResponse<Page<StudentCompetitionItem>> getStudentCompetitions(StudentCompetitionQuery query) {
        CompletableFuture<Page<StudentCompetitionItem>> future = CompletableFuture.supplyAsync(() -> {
            int pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
            int pageSize = Optional.ofNullable(query.getPageSize()).orElse(10);
            Page<StudentCompetitionItem> items = QueryChain.of(StudentCompetition.class)
                    .select(
                            COMPETITION.ALL_COLUMNS,
                            STUDENT_COMPETITION.ALL_COLUMNS,
                            STUDENT_BASIC.NAME.as("headerName"),
                            STUDENT_COMPETITION_AUDIT.STATE,
                            STUDENT_COMPETITION_AUDIT.REJECT_REASON
                    )
                    .from(STUDENT_COMPETITION)
                    .innerJoin(COMPETITION).on(COMPETITION.COMPETITION_ID.eq(STUDENT_COMPETITION.COMPETITION_ID))
                    .innerJoin(STUDENT_BASIC).on(STUDENT_BASIC.STUDENT_ID.eq(STUDENT_COMPETITION.HEADER_ID))
                    .innerJoin(STUDENT_COMPETITION_AUDIT).on(STUDENT_COMPETITION_AUDIT.STUDENT_COMPETITION_ID.eq(STUDENT_COMPETITION.STUDENT_COMPETITION_ID))
                    .innerJoin(STUDENT_COMPETITION_TEAM).on(STUDENT_COMPETITION_TEAM.STUDENT_COMPETITION_ID.eq(STUDENT_COMPETITION.STUDENT_COMPETITION_ID))
                    .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT_BASIC.MAJOR_ID))
                    .innerJoin(GRADE).on(GRADE.GRADE_ID.eq(STUDENT_BASIC.GRADE_ID))
                    .innerJoin(DEGREE).on(DEGREE.DEGREE_ID.eq(STUDENT_BASIC.DEGREE_ID))
                    .where(
                            STUDENT_COMPETITION.HEADER_ID.like(query.getSearch())
                                    .or(STUDENT_BASIC.NAME.like(query.getSearch()))
                                    .or(COMPETITION.COMPETITION_NAME.like(query.getSearch()))
                    )
                    // 竞赛性质
                    .and(COMPETITION.COMPETITION_NATURE.eq(query.getCompetitionNature()))
                    // 竞赛类别: A/B/C类
                    .and(COMPETITION.COMPETITION_TYPE.eq(query.getCompetitionType()))
                    // 获奖级别
                    .and(STUDENT_COMPETITION.LEVEL.eq(query.getLevel()))
                    .and(STUDENT_COMPETITION_AUDIT.STATE.eq(query.getState()))
                    .and(MAJOR.MAJOR_ID.eq(query.getMajorId()))
                    .and(GRADE.GRADE_ID.eq(query.getGradeId()))
                    .and(DEGREE.DEGREE_ID.eq(query.getDegreeId()))
                    .and(
                            STUDENT_COMPETITION.DATE.le(query.getEnd())
                                    .and(STUDENT_COMPETITION.DATE.ge(query.getStart()))
                    )
                    .pageAs(Page.of(pageNo, pageSize), StudentCompetitionItem.class);
            for (StudentCompetitionItem item : items.getRecords()) {
                String competitionId = item.getCompetitionId();
                if (competitionService.competitionNatureIsSolo(competitionId))
                    continue;
                String studentCompetitionId = item.getStudentCompetitionId();
                List<TeamItem> team = competitionTeamService.getTeamByStudentCompetitionId(studentCompetitionId);
                item.setTeam(team);
            }
            return items;
        }, readThreadPool);
        Page<StudentCompetitionItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }
}
