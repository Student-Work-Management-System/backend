package edu.guet.studentworkmanagementsystem.service.enrollmentInfo;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollmentInfo.EnrollmentQuery;
import edu.guet.studentworkmanagementsystem.entity.po.enrollment.EnrollmentInfo;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import org.springframework.web.multipart.MultipartFile;

public interface EnrollmentInfoService extends IService<EnrollmentInfo> {
    /**
     * 使用文件导入学业预警信息记录
     * @param multipartFile 文件源
     */
    <T> BaseResponse<T> importEnrollmentInfo(MultipartFile multipartFile);
    /**
     * 添加学业预警记录
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
     * @param pageNo 页号, 默认1
     * @param pageSize 页大小, 默认50
     */
    BaseResponse<Page<EnrollmentInfo>> getAllRecords(EnrollmentQuery query, int pageNo, int pageSize);
}
