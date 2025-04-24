package edu.guet.studentworkmanagementsystem.service.leave;

import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.AuditOperator;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeaveAudit;

public interface StudentLeaveAuditService extends IService<StudentLeaveAudit> {
    void addAudit(StudentLeaveAudit audit);
    <T> BaseResponse<T> counselorAudit(AuditOperator operator);
    <T> BaseResponse<T> leaderAudit(AuditOperator operator);
}
