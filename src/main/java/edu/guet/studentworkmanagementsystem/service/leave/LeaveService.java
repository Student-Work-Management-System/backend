package edu.guet.studentworkmanagementsystem.service.leave;


import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.*;
import edu.guet.studentworkmanagementsystem.entity.vo.leave.StudentLeaveItem;
import edu.guet.studentworkmanagementsystem.entity.vo.leave.StudentLeaveStatGroup;

import java.util.List;

public interface LeaveService {
    <T> BaseResponse<T> addStudentLeave(StudentLeaveRequest request);
    <T> BaseResponse<T> studentDestroyLeave(String leaveId);
    <T> BaseResponse<T> counselorAudit(AuditOperator operator);
    <T> BaseResponse<T> leaderAudit(AuditOperator operator);
    BaseResponse<Page<StudentLeaveItem>> getStudentOnwRecord(StudentLeaveQuery query);
    BaseResponse<Page<StudentLeaveItem>> getAuditRecord(AuditLeaveQuery query);
    BaseResponse<List<StudentLeaveStatGroup>> getStat(LeaveStatQuery query);
}