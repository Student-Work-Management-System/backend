package edu.guet.studentworkmanagementsystem.service.competition.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.*;
import edu.guet.studentworkmanagementsystem.entity.po.competition.*;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import edu.guet.studentworkmanagementsystem.entity.vo.competition.StudentCompetitionPassedRecord;
import edu.guet.studentworkmanagementsystem.entity.vo.competition.StudentCompetitionVO;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.competition.CompetitionMapper;
import edu.guet.studentworkmanagementsystem.mapper.competition.StudentCompetitionClaimMapper;
import edu.guet.studentworkmanagementsystem.mapper.competition.StudentCompetitionMapper;
import edu.guet.studentworkmanagementsystem.service.competition.CompetitionService;
import edu.guet.studentworkmanagementsystem.utils.JsonUtil;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import edu.guet.studentworkmanagementsystem.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

import static edu.guet.studentworkmanagementsystem.entity.po.competition.table.CompetitionTableDef.COMPETITION;
import static edu.guet.studentworkmanagementsystem.entity.po.competition.table.StudentCompetitionClaimTableDef.STUDENT_COMPETITION_CLAIM;
import static edu.guet.studentworkmanagementsystem.entity.po.competition.table.StudentCompetitionTableDef.STUDENT_COMPETITION;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentTableDef.STUDENT;

@Service
@Slf4j
public class CompetitionServiceImpl extends ServiceImpl<StudentCompetitionMapper, StudentCompetition> implements CompetitionService {
    @Autowired
    private CompetitionMapper competitionMapper;
    @Autowired
    private StudentCompetitionClaimMapper claimMapper;
    private static final String REJECT = "已拒绝";
    private static final String PASS = "已通过";
    private static final String WAITING = "审核中";
    private static final String SOLO = "单人";
    private static final String TEAM = "团队";
    @Override
    @Transactional
    public <T> BaseResponse<T> importCompetition(CompetitionList competitionList) {
        int size = competitionList.getCompetitions().size();
        int i = competitionMapper.insertBatch(competitionList.getCompetitions());
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> insertCompetition(Competition competition) {
        int i = competitionMapper.insert(competition);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> updateCompetition(Competition competition) {
        int update = competitionMapper.update(competition);
        if (update > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    public BaseResponse<Page<Competition>> getAllCompetitions(int pageNo, int pageSize) {
        Page<Competition> competitionPage = QueryChain.of(Competition.class)
                .page(Page.of(pageNo, pageSize));
        return ResponseUtil.success(competitionPage);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> deleteCompetition(String competitionId) {
        int i = competitionMapper.deleteById(competitionId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> insertStudentCompetition(StudentCompetitionDTO studentCompetitionDTO) throws JsonProcessingException {
        StudentCompetition studentCompetition = new StudentCompetition(studentCompetitionDTO);
        int i = mapper.insert(studentCompetition);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<List<StudentCompetitionVO>> getOwnStudentCompetition(String studentId) {
        List<StudentCompetitionVO> studentCompetitionVOList = QueryChain.of(Student.class)
                .select(STUDENT_COMPETITION.ALL_COLUMNS, COMPETITION.ALL_COLUMNS, STUDENT.NAME.as("headerName"))
                .from(STUDENT)
                .innerJoin(STUDENT_COMPETITION).on(STUDENT.STUDENT_ID.eq(STUDENT_COMPETITION.HEADER_ID))
                .innerJoin(STUDENT_COMPETITION_CLAIM).on(STUDENT.STUDENT_ID.eq(STUDENT_COMPETITION_CLAIM.STUDENT_ID))
                .innerJoin(COMPETITION).on(STUDENT_COMPETITION.COMPETITION_ID.eq(COMPETITION.COMPETITION_ID))
                .where(STUDENT_COMPETITION.HEADER_ID.eq(studentId).or(STUDENT_COMPETITION.MEMBERS.like(studentId)))
                .listAs(StudentCompetitionVO.class);
        return ResponseUtil.success(studentCompetitionVOList);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> auditStudentCompetition(CompetitionAuditDTO competitionAuditDTO) throws JsonProcessingException {
        String studentCompetitionId = competitionAuditDTO.getStudentCompetitionId();
        String uid = SecurityUtil.getUserCredentials().getUser().getUid();
        boolean update = UpdateChain.of(StudentCompetition.class)
                .set(STUDENT_COMPETITION.REVIEW_STATE, competitionAuditDTO.getReviewState(), StringUtils::hasLength)
                .set(STUDENT_COMPETITION.REJECT_REASON, competitionAuditDTO.getRejectReason(), StringUtils::hasLength)
                .set(STUDENT_COMPETITION.AUDITOR_ID, uid, StringUtils::hasLength)
                .where(STUDENT_COMPETITION.STUDENT_COMPETITION_ID.eq(studentCompetitionId))
                .update();
        HashMap<String, Object> map = QueryChain.of(Competition.class)
                .select(COMPETITION.COMPETITION_NATURE, STUDENT_COMPETITION.HEADER_ID)
                .from(COMPETITION)
                .innerJoin(STUDENT_COMPETITION).on(COMPETITION.COMPETITION_ID.eq(STUDENT_COMPETITION.COMPETITION_ID))
                .where(STUDENT_COMPETITION.STUDENT_COMPETITION_ID.eq(studentCompetitionId))
                .oneAs(HashMap.class);
        String competitionNature = map.get("competition_nature").toString();
        String headerId = map.get("header_id").toString();
        if (update) {
            if (stateHandler(competitionAuditDTO.getReviewState())) {
                if (Objects.isNull(competitionNature))
                    throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
                if (natureHandler(competitionNature)) {
                    String memberJson = mapper.selectOneById(studentCompetitionId).getMembers();
                    return insertStudentCompetitionAudit(convertToEntity(memberJson), studentCompetitionId);
                } else {
                    StudentCompetitionClaim studentCompetitionClaim = new StudentCompetitionClaim(studentCompetitionId, headerId);
                    int i = claimMapper.insert(studentCompetitionClaim);
                    if (i > 0)
                        return ResponseUtil.success();
                    throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
                }
            }
            return ResponseUtil.success();
        }
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Transactional
    public <T> BaseResponse<T> insertStudentCompetitionAudit(Member[] members, String studentCompetitionId) {
        ArrayList<StudentCompetitionClaim> claims = new ArrayList<>();
        for(Member member : members) {
            StudentCompetitionClaim studentCompetitionClaim = new StudentCompetitionClaim(studentCompetitionId, member.getStudentId());
            claims.add(studentCompetitionClaim);
        }
        int i = claimMapper.insertBatch(claims);
        if (i == claims.size())
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudentCompetition(String studentCompetitionId) {
        QueryWrapper wrapper = QueryWrapper.create().where(STUDENT_COMPETITION_CLAIM.STUDENT_COMPETITION_ID.eq(studentCompetitionId));
        long claimNumber = claimMapper.selectCountByQuery(wrapper);
        int i = claimMapper.deleteByQuery(wrapper);
        if (i == claimNumber) {
            int j = mapper.deleteById(studentCompetitionId);
            if (j > 0)
                return ResponseUtil.success();
        }
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<Page<StudentCompetitionVO>> getAllStudentCompetition(CompetitionQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        Page<StudentCompetitionVO> studentCompetitionVOPage = QueryChain.of(StudentCompetition.class)
                .select(STUDENT_COMPETITION.ALL_COLUMNS, COMPETITION.ALL_COLUMNS, STUDENT.NAME.as("headerName"))
                .where(STUDENT_COMPETITION.REVIEW_STATE.eq(WAITING))
                .innerJoin(STUDENT).on(STUDENT.STUDENT_ID.eq(STUDENT_COMPETITION.HEADER_ID))
                .innerJoin(MAJOR).on(STUDENT.MAJOR_ID.eq(MAJOR.MAJOR_ID))
                .innerJoin(COMPETITION).on(STUDENT_COMPETITION.COMPETITION_ID.eq(COMPETITION.COMPETITION_ID))
                .where(STUDENT.STUDENT_ID.like(query.getSearch())
                        .or(STUDENT.NAME.like(query.getSearch()))
                        .or(COMPETITION.COMPETITION_NAME.like(query.getSearch())))
                .and(STUDENT_COMPETITION.REVIEW_STATE.eq(query.getState()))
                .and(STUDENT.ENABLED.eq(query.getEnabled()))
                .and(STUDENT.GRADE_ID.eq(query.getGrade()))
                .and(STUDENT.MAJOR_ID.eq(query.getMajorId()))
                .and(STUDENT_COMPETITION.AWARD_DATE.ge(query.getStartDate()).and(STUDENT_COMPETITION.AWARD_DATE.le(query.getEndDate())))
                .pageAs(Page.of(pageNo, pageSize), StudentCompetitionVO.class);
        return ResponseUtil.success(studentCompetitionVOPage);
    }

    @Override
    public BaseResponse<Page<StudentCompetitionPassedRecord>> getAllPassedStudentCompetition(CompetitionQuery query) {
        int pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        int pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        List<StudentCompetitionPassedRecord> list = QueryChain.of(Student.class)
                .select(STUDENT.STUDENT_ID, STUDENT.NAME.as("studentName"))
                .from(STUDENT)
                .innerJoin(MAJOR).on(STUDENT.MAJOR_ID.eq(MAJOR.MAJOR_ID)).and(MAJOR.MAJOR_ID.eq(query.getMajorId()))
                .innerJoin(STUDENT_COMPETITION_CLAIM).on(STUDENT.STUDENT_ID.eq(STUDENT_COMPETITION_CLAIM.STUDENT_ID))
                .where(STUDENT.STUDENT_ID.like(query.getSearch()).or(STUDENT.NAME.like(query.getSearch())))
                .and(STUDENT.GRADE_ID.eq(query.getGrade()))
                .listAs(StudentCompetitionPassedRecord.class);
        list.forEach(item -> {
            String studentId = item.getStudentId();
            List<StudentCompetitionVO> studentCompetitionVOList = QueryChain.of(StudentCompetition.class)
                    .select(STUDENT_COMPETITION.ALL_COLUMNS, COMPETITION.ALL_COLUMNS, STUDENT.NAME.as("headerName"))
                    .where(STUDENT_COMPETITION.REVIEW_STATE.eq(PASS))
                    .innerJoin(STUDENT).on(STUDENT.STUDENT_ID.eq(STUDENT_COMPETITION.HEADER_ID))
                    .innerJoin(COMPETITION).on(STUDENT_COMPETITION.COMPETITION_ID.eq(COMPETITION.COMPETITION_ID))
                    .where(STUDENT_COMPETITION.HEADER_ID.eq(studentId).or(STUDENT_COMPETITION.MEMBERS.like(studentId)))
                    .and(STUDENT.ENABLED.eq(query.getEnabled()))
                    .and(STUDENT_COMPETITION.AWARD_DATE.ge(query.getStartDate()).and(STUDENT_COMPETITION.AWARD_DATE.le(query.getEndDate())))
                    .listAs(StudentCompetitionVO.class);
            item.setAwards(studentCompetitionVOList);
        });
        Page<StudentCompetitionPassedRecord> page = new Page<>(list, pageNo, pageSize, list.size());
        return ResponseUtil.success(page);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> clearStudentCompetitionState(ClearStudentCompetition clearStudentCompetition) {
        String id = SecurityUtil.getUserPrincipal();
        StudentCompetition one = QueryChain.of(StudentCompetition.class)
                .where(STUDENT_COMPETITION.STUDENT_COMPETITION_ID.eq(clearStudentCompetition.getStudentCompetitionId()))
                .and(STUDENT_COMPETITION.HEADER_ID.eq(id).or(STUDENT_COMPETITION.MEMBERS.like(id)))
                .one();
        if (Objects.isNull(one) || !Objects.equals(one.getReviewState(), REJECT))
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        String memberJson;
        try {
            memberJson = JsonUtil.getMapper().writeValueAsString(one.getMembers());
        } catch (JsonProcessingException e) {
            log.error("CompetitionServiceImpl#clearStudentCompetitionState出现JSON解析异常: {}", e.getMessage());
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        }
        boolean update = UpdateChain.of(StudentCompetition.class)
                .set(STUDENT_COMPETITION.EVIDENCE, clearStudentCompetition.getEvidence(), StringUtils::hasLength)
                .set(STUDENT_COMPETITION.AWARD_DATE, clearStudentCompetition.getAwardDate(), Objects::nonNull)
                .set(STUDENT_COMPETITION.AWARD_LEVEL, clearStudentCompetition.getAwardLevel(), StringUtils::hasLength)
                .set(STUDENT_COMPETITION.MEMBERS, memberJson, StringUtils::hasLength)
                .set(STUDENT_COMPETITION.AUDITOR_ID, null)
                .set(STUDENT_COMPETITION.REJECT_REASON, null)
                .set(STUDENT_COMPETITION.REVIEW_STATE, WAITING)
                .where(STUDENT_COMPETITION.STUDENT_COMPETITION_ID.eq(clearStudentCompetition.getStudentCompetitionId()))
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    private Member[] convertToEntity(String membersJson) throws JsonProcessingException {
        return JsonUtil.getMapper().readValue(membersJson, new TypeReference<>(){});
    }

    private Boolean stateHandler(String reviewState) {
        switch (reviewState) {
            case PASS -> {
                return true;
            }
            case REJECT -> {
                return false;
            }
            default -> throw new ServiceException(ServiceExceptionEnum.SELECT_NOT_IN);
        }
    }
    private Boolean natureHandler(String nature) {
        switch (nature) {
            case SOLO -> {
                return false;
            }
            case TEAM -> {
                return true;
            }
            default -> throw new ServiceException(ServiceExceptionEnum.SELECT_NOT_IN);
        }
    }
}
