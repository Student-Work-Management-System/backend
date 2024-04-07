package edu.guet.studentworkmanagementsystem.service.status;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.status.StatusList;
import edu.guet.studentworkmanagementsystem.entity.dto.status.StatusQuery;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.entity.po.status.StudentStatus;
import edu.guet.studentworkmanagementsystem.entity.vo.status.StudentStatusVO;

import java.util.List;


public interface StatusService extends IService<StudentStatus> {
    /**
     * 批量导入学生学籍变动信息记录
     * @param studentStatuses 学生学籍信息列表
     */
    <T> BaseResponse<T> importStudentStatus(StatusList statusList);
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
     * 删除学生学籍变动记录
     * @param studentStatusId 学生学籍变动表id
     */
    <T> BaseResponse<T> deleteStudentStatus(String studentStatusId);
    /**
     * 分页查询学生学籍变动信息
     * <br/>
     * @param query 查询参数
     */
    BaseResponse<Page<StudentStatusVO>> getAllRecords(StatusQuery query);
}
