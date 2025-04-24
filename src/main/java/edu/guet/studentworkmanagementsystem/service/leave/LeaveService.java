package edu.guet.studentworkmanagementsystem.service.leave;


import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.AuditOperator;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.AuditLeaveQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.StudentLeaveQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.StudentLeaveRequest;
import edu.guet.studentworkmanagementsystem.entity.vo.leave.StudentLeaveItem;

public interface LeaveService {
    <T> BaseResponse<T> addStudentLeave(StudentLeaveRequest request);
    <T> BaseResponse<T> studentDestroyLeave(String leaveId);
    <T> BaseResponse<T> counselorAudit(AuditOperator operator);
    <T> BaseResponse<T> leaderAudit(AuditOperator operator);
    BaseResponse<Page<StudentLeaveItem>> getStudentOnwRecord(StudentLeaveQuery query);
    BaseResponse<Page<StudentLeaveItem>> getAuditRecord(AuditLeaveQuery query);
}
