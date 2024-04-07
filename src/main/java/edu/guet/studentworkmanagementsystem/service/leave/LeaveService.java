package edu.guet.studentworkmanagementsystem.service.leave;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.LeaveList;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.LeaveQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.StudentLeaveAuditDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.StudentLeaveDTO;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeave;
import edu.guet.studentworkmanagementsystem.entity.vo.leave.StudentLeaveVO;


public interface LeaveService extends IService<StudentLeave> {
    /**
     * 批量导入学生请假信息
     * @param studentLeaves 学生请假信息列表
     */
    <T> BaseResponse<T> importStudentLeave(LeaveList studentLeaves);
     /**
     * 对象添加学生请假信息
     * @param studentLeave 学生请假信息记录对象
     */
     <T> BaseResponse<T> insertStudentLeave(StudentLeave studentLeave);
    /**
     * 修改学生请假信息的信息
     * @param studentLeaveDTO 修改的学生请假信息
     */
    <T> BaseResponse<T> updateStudentLeave(StudentLeaveDTO studentLeaveDTO);
    /**
     * 删除学生请假信息记录(需要考虑外检约束)
     * @param studentLeaveId 学生请假信息记录id
     */
    <T> BaseResponse<T> deleteStudentLeave(String studentLeaveId);
    /**
     * 分页查询学生请假信息
     * <br/>
     * @param query 查询参数
     * @return 学生就业信息
     */
    BaseResponse<Page<StudentLeaveVO>> getStudentLeave(LeaveQuery query);
    /**
     * 添加审核记录(注: 审核成功后不允许修改)
     * @param studentLeaveAuditDTO 请假审核记录
     */
    <T> BaseResponse<T> audiStudentLeave(StudentLeaveAuditDTO studentLeaveAuditDTO);
    <T> BaseResponse<T> insertStudentLeaveAuditRecord(String studentLeaveId);
}
