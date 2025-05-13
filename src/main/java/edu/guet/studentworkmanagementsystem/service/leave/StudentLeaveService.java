package edu.guet.studentworkmanagementsystem.service.leave;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.AuditLeaveQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.LeaveStatQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.StudentLeaveQuery;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeave;
import edu.guet.studentworkmanagementsystem.entity.vo.leave.StudentLeaveItem;
import edu.guet.studentworkmanagementsystem.entity.vo.leave.StudentLeaveStatGroup;

import java.util.List;

public interface StudentLeaveService extends IService<StudentLeave> {
    String addStudentLeave(StudentLeave studentLeave);
    <T> BaseResponse<T> destroyLeave(String leaveId);
    BaseResponse<Page<StudentLeaveItem>> getOwnLeaves(StudentLeaveQuery query);
    BaseResponse<Page<StudentLeaveItem>> getLeaves(AuditLeaveQuery query);
    BaseResponse<List<StudentLeaveStatGroup>> getStat(LeaveStatQuery query);
}
