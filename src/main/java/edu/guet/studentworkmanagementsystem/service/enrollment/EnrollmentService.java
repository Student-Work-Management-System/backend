package edu.guet.studentworkmanagementsystem.service.enrollment;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentList;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollmentInfo.EnrollmentQuery;
import edu.guet.studentworkmanagementsystem.entity.po.enrollment.Enrollment;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;

public interface EnrollmentService extends IService<Enrollment> {
    /**
     * 批量导入招生信息记录
     * @param enrollmentList 招生信息列表
     */
    <T> BaseResponse<T> importEnrollment(EnrollmentList enrollmentList);
    /**
     * 添加招生记录
     * @param enrollment 招生信息
     */
    BaseResponse<Scholarship> insertEnrollment(Enrollment enrollment);
    /**
     * 修改招生信息记录
     * @param enrollment 待修改的招生信息记录
     */
    <T> BaseResponse<T> updateEnrollment(Enrollment enrollment);
    /**
     * 删除招生信息记录
     * @param enrollmentInfoId 招生信息表id
     */
    <T> BaseResponse<T> deleteEnrollment(String enrollmentInfoId);
    /**
     * 分页查询招生信息
     * <br/>
     * @param query 查询参数
     */
    BaseResponse<Page<Enrollment>> getAllRecords(EnrollmentQuery query);
}
