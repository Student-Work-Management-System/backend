package edu.guet.studentworkmanagementsystem.service.student;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentQuery;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentArchive;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentTableItem;

/**
 * 因学生相关信息太大, 学生表信息拆分为StudentBasic和StudentDetail两个子模块
 */
public interface StudentService extends IService<Student> {
    /**
     * 批量导入学生
     * @param students 学生列表
     */
    <T> BaseResponse<T> importStudent(ValidateList<Student> students);
    /**
     * 单个导入学生
     * @param student 学生对象
     */
    <T> BaseResponse<T> addStudent(Student student);
    /**
     * 分页获取学生
     * @param query 学生查询参数
     * @return 学生列表
     */
    BaseResponse<Page<StudentTableItem>> getStudents(StudentQuery query);
    BaseResponse<StudentArchive> getStudent(String studentId);
    /**
     * 修改学生信息
     * @param student 学生信息对象
     */
    <T> BaseResponse<T> updateStudent(Student student);
    /**
     * 使用学号删除学生
     * @param studentId 学号
     */
    <T> BaseResponse<T> deleteStudent(String studentId);
    <T> BaseResponse<T> recoveryStudent(String studentId);
    <T> BaseResponse<T> validateHeadTeacherExists(String headTeacherUsername);
}
