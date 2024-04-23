package edu.guet.studentworkmanagementsystem.service.employment;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.employment.*;
import edu.guet.studentworkmanagementsystem.entity.po.employment.StudentEmployment;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.StudentEmploymentVO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface EmploymentService extends IService<StudentEmployment> {
    /**
     * 批量导入学生就业信息
     * @param insertEmploymentDTOList 学生就业信息列表
     */
    <T> BaseResponse<T> importStudentEmployment(InsertEmploymentDTOList insertEmploymentDTOList);
    /**
     * 对象添加就业信息
     * @param insertStudentEmploymentDTO 学生就业信息对象
     */
    <T> BaseResponse<T> insertStudentEmployment(InsertStudentEmploymentDTO insertStudentEmploymentDTO);
    /**
     * 分页查询学生就业信息
     * <br/>
     * @param query 查询参数
     * @return 学生就业信息
     */
    BaseResponse<Page<StudentEmploymentVO>> getStudentEmployment(EmploymentQuery query);
    /**
     * 修改学生就业信息记录
     * @param updateStudentEmploymentDTO 学生就业信息对象(学号定位)
     */
    <T> BaseResponse<T> updateStudentEmployment(UpdateStudentEmploymentDTO updateStudentEmploymentDTO);
    /**
     * 删除学生就业信息
     */
    <T> BaseResponse<T> deleteStudentEmployment(String studentEmploymentId);
    /**
     * 就业信息(文件下载)
     */
    void download(EmploymentStatQuery query, HttpServletResponse response);
    /**
     * 统计就业信息
     */
    <T> BaseResponse<T> statistics(EmploymentStatQuery query);
}
