package edu.guet.studentworkmanagementsystem.service.competition.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.CompetitionAuditDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.CompetitionList;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.CompetitionQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.StudentCompetitionDTO;
import edu.guet.studentworkmanagementsystem.entity.po.competition.Competition;
import edu.guet.studentworkmanagementsystem.entity.po.competition.Members;
import edu.guet.studentworkmanagementsystem.entity.po.competition.StudentCompetition;
import edu.guet.studentworkmanagementsystem.entity.po.competition.StudentCompetitionClaim;
import edu.guet.studentworkmanagementsystem.entity.vo.competition.StudentCompetitionVO;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.competition.CompetitionMapper;
import edu.guet.studentworkmanagementsystem.mapper.competition.StudentCompetitionClaimMapper;
import edu.guet.studentworkmanagementsystem.mapper.competition.StudentCompetitionMapper;
import edu.guet.studentworkmanagementsystem.service.competition.CompetitionService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static edu.guet.studentworkmanagementsystem.entity.po.competition.table.StudentCompetitionTableDef.STUDENT_COMPETITION;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentTableDef.STUDENT;

@Service
public class CompetitionServiceImpl extends ServiceImpl<StudentCompetitionMapper, StudentCompetition> implements CompetitionService {
    @Autowired
    private CompetitionMapper competitionMapper;
    @Autowired
    private StudentCompetitionClaimMapper claimMapper;
    @Override
    @Transactional
    public BaseResponse<List<Competition>> importCompetition(CompetitionList competitionList) {
        int size = competitionList.getCompetitions().size();
        int i = competitionMapper.insertBatch(competitionList.getCompetitions());
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public BaseResponse<Competition> insertCompetition(Competition competition) {
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
        List<StudentCompetitionVO> studentCompetitionVOS = QueryChain.of(StudentCompetition.class)
                .select(STUDENT_COMPETITION.ALL_COLUMNS, STUDENT.NAME.as("headerName"), STUDENT.STUDENT_ID.as("headerId"))
                .innerJoin(STUDENT).on(STUDENT.STUDENT_ID.eq(STUDENT_COMPETITION.HEADER_ID))
                .where(STUDENT_COMPETITION.HEADER_ID.eq(studentId))
                .listAs(StudentCompetitionVO.class);
        return ResponseUtil.success(studentCompetitionVOS);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> auditStudentCompetition(CompetitionAuditDTO competitionAuditDTO) {
        return null;
    }

    @Override
    @Transactional
    public boolean insertStudentCompetitionAudit(Members members, String studentCompetitionId) {
        ArrayList<StudentCompetitionClaim> claims = new ArrayList<>();
        members.getMembers().forEach(member -> {
            StudentCompetitionClaim studentCompetitionClaim = new StudentCompetitionClaim(studentCompetitionId, member.getStudentId());
            claims.add(studentCompetitionClaim);
        });
        int i = claimMapper.insertBatch(claims);
        return i == claims.size();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudentCompetition(String competitionId, String studentId) {
        return null;
    }

    @Override
    @Transactional
    public BaseResponse<Page<StudentCompetitionVO>> getAllStudentCompetition(CompetitionQuery query) {
        return null;
    }
}
