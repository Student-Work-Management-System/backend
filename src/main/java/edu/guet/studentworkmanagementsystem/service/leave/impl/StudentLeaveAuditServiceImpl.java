package edu.guet.studentworkmanagementsystem.service.leave.impl;

import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.AuditOperator;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeaveAudit;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.leave.StudentLeaveAuditMapper;
import edu.guet.studentworkmanagementsystem.service.leave.StudentLeaveAuditService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static edu.guet.studentworkmanagementsystem.entity.po.leave.table.StudentLeaveAuditTableDef.STUDENT_LEAVE_AUDIT;

@Service
public class StudentLeaveAuditServiceImpl extends ServiceImpl<StudentLeaveAuditMapper, StudentLeaveAudit> implements StudentLeaveAuditService {

    @Override
    @Transactional
    public void addAudit(StudentLeaveAudit audit) {
        int i = mapper.insert(audit);
        // todo: 通知辅导员(定时邮件任务)
        if (i == 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> counselorAudit(AuditOperator operator) {
        boolean update = UpdateChain.of(StudentLeaveAudit.class)
                .set(STUDENT_LEAVE_AUDIT.COUNSELOR_ID, operator.getUsername())
                .set(STUDENT_LEAVE_AUDIT.COUNSELOR_HANDLE_STATE, operator.getState())
                .set(STUDENT_LEAVE_AUDIT.COUNSELOR_HANDLE_TIME, LocalDate.now())
                .where(STUDENT_LEAVE_AUDIT.AUDIT_ID.eq(operator.getAuditId()))
                .update();
        if (!update)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        if (operator.isNeedNoticeStudent()) {
            // todo: 通知学生完成审批
        }
        if (operator.isNeedNoticeLeader()) {
            // todo: 邮件通知领导审核
        }
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> leaderAudit(AuditOperator operator) {
        boolean update = UpdateChain.of(StudentLeaveAudit.class)
                .set(STUDENT_LEAVE_AUDIT.LEADER_ID, operator.getUsername())
                .set(STUDENT_LEAVE_AUDIT.LEADER_HANDLE_STATE, operator.getState())
                .set(STUDENT_LEAVE_AUDIT.LEADER_HANDLE_TIME, LocalDate.now())
                .where(STUDENT_LEAVE_AUDIT.AUDIT_ID.eq(operator.getAuditId()))
                .update();
        if (!update)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        if (operator.isNeedNoticeStudent()) {
            // todo: 通知学生审批完成
        }
        return ResponseUtil.success();
    }
}
