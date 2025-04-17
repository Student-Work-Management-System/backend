package edu.guet.studentworkmanagementsystem.service.student;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.enrollment.Enrollment;
import edu.guet.studentworkmanagementsystem.entity.po.student.HeaderTeacher;
import edu.guet.studentworkmanagementsystem.entity.vo.enrollment.EnrollmentItem;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentArchive;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentBasicItem;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentStatItem;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentTableItem;

import java.util.List;

/**
 * 因学生相关信息太大, 学生表信息拆分为StudentBasic和StudentDetail两个子模块
 */
public interface StudentService {
    /**
     * 批量导入学生
     */
    <T> BaseResponse<T> importStudent(ValidateList<Enrollment> enrollments);
    /**
     * 分页获取学生
     * @param query 学生查询参数
     * @return 学生列表
     */
    BaseResponse<Page<EnrollmentItem>> getStudents(EnrollmentQuery query);
    BaseResponse<StudentArchive> getStudentArchive(String studentId);
    /**
     * 修改学生信息(从档案上修改)
     */
    <T> BaseResponse<T> updateStudent(Enrollment enrollment);
    /**
     * 使用学号删除学生
     * @param studentId 学号
     */
    <T> BaseResponse<T> deleteStudent(String studentId);
    <T> BaseResponse<T> recoveryStudent(String studentId);
    BaseResponse<List<StudentStatItem>> getStudentStatus(StudentStatQuery query);
    BaseResponse<List<HeaderTeacher>> getHeaderTeachers();
    BaseResponse<List<StudentBasicItem>> getStudentBasic(String studentId);
    BaseResponse<List<StudentBasicItem>> getStudentCompetitionTeam(List<String> studentIds);
}
