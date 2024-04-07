package edu.guet.studentworkmanagementsystem.service.enrollmentInfo;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentInfoList;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollmentInfo.EnrollmentQuery;
import edu.guet.studentworkmanagementsystem.entity.po.enrollment.EnrollmentInfo;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;

import java.util.List;

public interface EnrollmentInfoService extends IService<EnrollmentInfo> {
    /**
     * 批量导入招生信息记录
     * @param enrollmentInfos 招生信息列表
     */
    <T> BaseResponse<T> importEnrollmentInfo(EnrollmentInfoList enrollmentInfos);
    /**
     * 添加招生记录
     * @param enrollmentInfo 招生信息
     */
    BaseResponse<Scholarship> insertEnrollmentInfo(EnrollmentInfo enrollmentInfo);
    /**
     * 修改招生信息记录
     * @param enrollmentInfo 待修改的招生信息记录
     */
    <T> BaseResponse<T> updateEnrollmentInfo(EnrollmentInfo enrollmentInfo);
    /**
     * 删除招生信息记录
     * @param enrollmentInfoId 招生信息表id
     */
    <T> BaseResponse<T> deleteEnrollmentInfo(String enrollmentInfoId);
    /**
     * 分页查询招生信息
     * <br/>
     * @param query 查询参数
     */
    BaseResponse<Page<EnrollmentInfo>> getAllRecords(EnrollmentQuery query);
}
