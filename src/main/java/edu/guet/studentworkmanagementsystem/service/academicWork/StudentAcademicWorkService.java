package edu.guet.studentworkmanagementsystem.service.academicWork;

import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.StudentAcademicWorkDTO;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.AcademicWork;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.StudentAcademicWork;
import org.springframework.web.multipart.MultipartFile;


public interface StudentAcademicWorkService extends IService<StudentAcademicWork> {
     /**
     * 使用文件导入学生学术作品信息
     * @param multipartFile 文件源
     */
    <T> BaseResponse<T> importStudentAcademicWork(MultipartFile multipartFile);
    /**
     * 单个上传学生学术著作,其中存在{@link AcademicWork 学术著作接口}
     * <br/>
     * 在插入时同时根据字段{@link StudentAcademicWork 学生学术作品类}的academicWorkType属性生成不同的实例, 具体见数据库
     * @param studentAcademicWorkDTO 学生学术著作上报对象
     */
    <T> BaseResponse<T> insertStudentAcademicWork(StudentAcademicWorkDTO studentAcademicWorkDTO);
}
