package edu.guet.studentworkmanagementsystem.service.leave;

import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeave;

public interface StudentLeaveService extends IService<StudentLeave> {
    void addStudentLeave(StudentLeave studentLeave);
    <T> BaseResponse<T> destroyLeave(String leaveId);
}
