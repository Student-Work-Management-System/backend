package edu.guet.studentworkmanagementsystem.service.employment;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.employment.EmploymentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.employment.StudentEmploymentDTO;
import edu.guet.studentworkmanagementsystem.entity.po.employment.StudentEmployment;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.StudentEmploymentVO;
import org.springframework.web.multipart.MultipartFile;

public interface EmploymentService extends IService<StudentEmployment> {
    /**
     * 使用文件导入学生就业信息
     * @param multipartFile 文件源
     */
    <T> BaseResponse<T> importStudentEmployment(MultipartFile multipartFile);
    /**
     * 对象添加就业信息
     * @param studentEmploymentDTO 学生就业信息对象
     */
    <T> BaseResponse<T> insertStudentEmployment(StudentEmploymentDTO studentEmploymentDTO);
    /**
     * 分页查询学生就业信息
     * <br/>
     * @param query 查询参数
     * @param pageNo 页号, 默认1
     * @param pageSize 页大小, 默认50
     * @return 学生就业信息
     */
    BaseResponse<Page<StudentEmploymentVO>> getStudentEmployment(EmploymentQuery query, int pageNo, int pageSize);
    /**
     * 修改学生就业信息记录
     * @param studentEmploymentDTO 学生就业信息对象(学号定位)
     */
    <T> BaseResponse<T> updateStudentEmployment(StudentEmploymentDTO studentEmploymentDTO);
    /**
     * 删除学生就业信息
     */
    <T> BaseResponse<T> deleteStudentEmployment(String studentId);
    // todo: 统计就业信息
}
