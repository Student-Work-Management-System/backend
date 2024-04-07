package edu.guet.studentworkmanagementsystem.service.student;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentList;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentQuery;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentVO;

import java.util.List;

public interface StudentService extends IService<Student> {
    /**
     * 批量导入学生
     * @param studentList 学生列表
     */
    <T> BaseResponse<T> importStudent(StudentList studentList);
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
    BaseResponse<Page<StudentVO>> getStudents(StudentQuery query);
    /**
     * 修改学生信息
     * @param studentDTO 学生信息对象
     */
    <T> BaseResponse<T> updateStudent(StudentDTO studentDTO);
    /**
     * 使用学号删除学生
     * @param studentId 学号
     */
    <T> BaseResponse<T> deleteStudent(String studentId);
}
