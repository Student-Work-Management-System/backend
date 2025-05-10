package edu.guet.studentworkmanagementsystem.service.enrollment;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.enrollment.Enrollment;
import edu.guet.studentworkmanagementsystem.entity.vo.enrollment.EnrollmentStatItem;
import edu.guet.studentworkmanagementsystem.entity.vo.enrollment.EnrollmentItem;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;

public interface EnrollmentService extends IService<Enrollment> {
    /**
     * 批量导入招生信息记录
     */
    <T> BaseResponse<T> importEnrollment(ValidateList<Enrollment> enrollments);
    /**
     * 修改招生信息记录
     * @param enrollment 待修改的招生信息记录
     */
    <T> BaseResponse<T> updateEnrollment(Enrollment enrollment);
    /**
     * 删除/恢复学生学籍信息记录
     */
    <T> BaseResponse<T> deleteEnrollment(String studentId);
    <T> BaseResponse<T> recoveryEnrollment(String studentId);
    /**
     * 分页查询招生信息
     * <br/>
     * @param query 查询参数
     */
    BaseResponse<Page<EnrollmentItem>>  getAllRecords(EnrollmentQuery query);
    BaseResponse<EnrollmentItem> getOwnEnrollment(String studentId);
}
