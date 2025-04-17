package edu.guet.studentworkmanagementsystem.service.status;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.status.StudentStatusQuery;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.entity.po.status.Status;
import edu.guet.studentworkmanagementsystem.entity.po.status.StudentStatus;
import edu.guet.studentworkmanagementsystem.entity.vo.status.StudentStatusItem;

import java.util.List;
import java.util.Set;


public interface StatusService extends IService<StudentStatus> {
    BaseResponse<List<Status>> getAllStatus();
    <T> BaseResponse<T> addStatus(Status status);
    <T> BaseResponse<T> updateStatus(Status status);
    <T> BaseResponse<T> deleteStatus(String statusId);
    /**
     * 批量导入学生学籍变动信息记录
     */
    <T> BaseResponse<T> importStudentStatus(ValidateList<StudentStatus> studentStatuses);
    boolean enrollmentStudent(Set<String> studentIds);
    /**
     * 添加学生学籍变动记录
     * @param studentStatus 学籍变动信息
     */
    BaseResponse<Scholarship> insertStudentStatus(StudentStatus studentStatus);
    /**
     * 修改学生学籍变动记录
     * @param studentStatus 待修改的学生学籍变动记录
     */
    <T> BaseResponse<T> updateStudentStatus(StudentStatus studentStatus);
    /**
     * 分页查询学生学籍变动信息
     * <br/>
     * @param query 查询参数
     */
    BaseResponse<Page<StudentStatusItem>> getAllRecords(StudentStatusQuery query);
    BaseResponse<List<StudentStatusItem>> getStudentStatusDetail(String studentId);
}
