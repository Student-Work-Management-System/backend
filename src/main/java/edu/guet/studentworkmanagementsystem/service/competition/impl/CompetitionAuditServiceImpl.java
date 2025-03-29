package edu.guet.studentworkmanagementsystem.service.competition.impl;

import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.po.competition.StudentCompetitionAudit;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.competition.CompetitionAuditMapper;
import edu.guet.studentworkmanagementsystem.service.competition.CompetitionAuditService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static edu.guet.studentworkmanagementsystem.entity.po.competition.table.StudentCompetitionAuditTableDef.STUDENT_COMPETITION_AUDIT;

@Service
public class CompetitionAuditServiceImpl extends ServiceImpl<CompetitionAuditMapper, StudentCompetitionAudit> implements CompetitionAuditService {

    @Override
    @Transactional
    public boolean insertCompetitionAudit(StudentCompetitionAudit studentCompetitionAudit) {
        return mapper.insert(studentCompetitionAudit) > 0;
    }

    @Override
    @Transactional
    public boolean deleteCompetitionAudit(String studentCompetitionId) {
        return mapper.deleteById(studentCompetitionId) > 0;
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateCompetitionAudit(ValidateList<StudentCompetitionAudit> studentCompetitionAudits) {
        studentCompetitionAudits.forEach((studentCompetitionAudit) -> {
            boolean update = UpdateChain.of(StudentCompetitionAudit.class)
                    .set(STUDENT_COMPETITION_AUDIT.STATE, studentCompetitionAudit.getState(), !Objects.isNull(studentCompetitionAudit.getState()))
                    .set(STUDENT_COMPETITION_AUDIT.REJECT_REASON, studentCompetitionAudit.getRejectReason(), StringUtils::hasLength)
                    .set(STUDENT_COMPETITION_AUDIT.OPERATOR_ID, studentCompetitionAudit.getOperatorId(), StringUtils::hasLength)
                    .set(STUDENT_COMPETITION_AUDIT.OPERATOR_TIME, LocalDate.now())
                    .where(STUDENT_COMPETITION_AUDIT.STUDENT_COMPETITION_ID.eq(studentCompetitionAudit.getStudentCompetitionId()))
                    .update();
            if (!update)
                throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        });
        return ResponseUtil.success();
    }
}
