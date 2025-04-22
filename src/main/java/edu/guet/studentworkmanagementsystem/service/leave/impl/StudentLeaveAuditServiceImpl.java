package edu.guet.studentworkmanagementsystem.service.leave.impl;

import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.AuditOperator;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeave;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeaveAudit;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.leave.StudentLeaveAuditMapper;
import edu.guet.studentworkmanagementsystem.service.leave.StudentLeaveAuditService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import edu.guet.studentworkmanagementsystem.utils.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static edu.guet.studentworkmanagementsystem.entity.po.leave.table.StudentLeaveAuditTableDef.STUDENT_LEAVE_AUDIT;
import static edu.guet.studentworkmanagementsystem.entity.po.leave.table.StudentLeaveTableDef.STUDENT_LEAVE;

@Service
public class StudentLeaveAuditServiceImpl extends ServiceImpl<StudentLeaveAuditMapper, StudentLeaveAudit> implements StudentLeaveAuditService {

    @Override
    @Transactional
    public void addAudit(StudentLeaveAudit audit) {
        int i = mapper.insert(audit);
        if (i == 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> counselorAudit(AuditOperator operator) {
        boolean update = UpdateChain.of(StudentLeaveAudit.class)
                .set(STUDENT_LEAVE_AUDIT.COUNSELOR_ID, operator.getUsername())
                .set(STUDENT_LEAVE_AUDIT.COUNSELOR_STATE, operator.getState())
                .set(STUDENT_LEAVE_AUDIT.COUNSELOR_HANDLE_TIME, LocalDate.now())
                .where(STUDENT_LEAVE_AUDIT.AUDIT_ID.eq(operator.getAuditId()))
                .update();
        if (!update)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        if (operator.isHasNext()) {
            // todo: 邮件通知领导审核
        }
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> leaderAudit(AuditOperator operator) {
        boolean update = UpdateChain.of(StudentLeaveAudit.class)
                .set(STUDENT_LEAVE_AUDIT.LEADER_ID, operator.getUsername())
                .set(STUDENT_LEAVE_AUDIT.LEADER_STATE, operator.getState())
                .set(STUDENT_LEAVE_AUDIT.LEADER_HANDLE_TIME, LocalDate.now())
                .where(STUDENT_LEAVE_AUDIT.AUDIT_ID.eq(operator.getAuditId()))
                .update();
        if (!update)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> revokedLeaveAudit(String leaveId) {
        String username = SecurityUtil.getUserCredentials().getUsername();
        StudentLeave one = QueryChain.of(StudentLeave.class)
                .where(STUDENT_LEAVE.LEAVE_ID.eq(leaveId))
                .one();
        if (!one.getStudentId().equals(username))
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        UpdateChain.of(StudentLeaveAudit.class)
                .set(STUDENT_LEAVE_AUDIT.REVOKED, false)
                .where(STUDENT_LEAVE_AUDIT.LEAVE_ID.eq(leaveId))
                .update();
        return ResponseUtil.success();
    }
}
