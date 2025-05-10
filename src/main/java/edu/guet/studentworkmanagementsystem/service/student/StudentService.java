package edu.guet.studentworkmanagementsystem.service.student;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.enrollment.Enrollment;
import edu.guet.studentworkmanagementsystem.entity.po.student.HeaderTeacher;
import edu.guet.studentworkmanagementsystem.entity.vo.enrollment.EnrollmentItem;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentArchive;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentBasicItem;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentStatGroup;

import java.util.List;

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
    BaseResponse<EnrollmentItem> getOwnEnrollment(String studentId);
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
    BaseResponse<List<StudentStatGroup>> getStudentStat(StudentStatQuery query);
    BaseResponse<List<HeaderTeacher>> getHeaderTeachers();
    BaseResponse<List<StudentBasicItem>> getStudentBasic(String studentId);
    BaseResponse<List<StudentBasicItem>> getStudentCompetitionTeam(List<String> studentIds);
}
