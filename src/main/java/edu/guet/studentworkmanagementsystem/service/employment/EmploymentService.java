package edu.guet.studentworkmanagementsystem.service.employment;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.employment.*;
import edu.guet.studentworkmanagementsystem.entity.po.employment.StudentEmployment;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.StudentEmploymentStatGroup;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.StudentEmploymentItem;

import java.util.List;

public interface EmploymentService extends IService<StudentEmployment> {
    /**
     * 批量导入学生就业信息
     */
    <T> BaseResponse<T> importStudentEmployment(ValidateList<StudentEmployment> studentEmployments);
    /**
     * 分页查询学生就业信息
     * <br/>
     * @param query 查询参数
     * @return 学生就业信息
     */
    BaseResponse<Page<StudentEmploymentItem>> getStudentEmployment(EmploymentQuery query);
    /**
     * 修改学生就业信息记录
     */
    <T> BaseResponse<T> updateStudentEmployment(StudentEmployment studentEmployment);
    /**
     * 删除学生就业信息
     */
    <T> BaseResponse<T> deleteStudentEmployment(String studentEmploymentId);
    BaseResponse<List<StudentEmploymentStatGroup>> getStat(EmploymentStatQuery query);
}
