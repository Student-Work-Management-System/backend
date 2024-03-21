package edu.guet.studentworkmanagementsystem.service.student;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentDTO;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService extends IService<Student> {
    /**
     * 使用文件导入学生, 返回处理结果
     * @param multipartFile 文件源
     */
    <T> BaseResponse<T> importStudent(MultipartFile multipartFile);
    /**
     * 由前端处理完毕文件后导入学生, 返回处理结果
     * @param students 学生实例
     */
    <T> BaseResponse<T> importStudent(List<Student> students);
    /**
     * 分页获取学生
     * @param pageNo 页号,默认: 1
     * @param pageSize 每页大小, 默认: 50
     * @return 学生列表
     */
    BaseResponse<Page<Student>> getStudents(int pageNo, int pageSize);
    /**
     * 修改学生信息
     * @param student 来自前端传输的学生对象, 存在某个属性为空(使用学号定位学生, 默认姓名、身份证没有出错)
     */
    <T> BaseResponse<T> updateStudent(StudentDTO student);
}
