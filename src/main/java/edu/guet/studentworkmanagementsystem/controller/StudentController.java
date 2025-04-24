package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.enrollment.Enrollment;
import edu.guet.studentworkmanagementsystem.entity.po.student.HeaderTeacher;
import edu.guet.studentworkmanagementsystem.entity.vo.enrollment.EnrollmentItem;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentBasicItem;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentStatItem;
import edu.guet.studentworkmanagementsystem.service.student.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PreAuthorize("hasAuthority('student:select')")
    @PostMapping("/gets")
    public BaseResponse<Page<EnrollmentItem>> getAllStudent(@RequestBody EnrollmentQuery query) {
        return studentService.getStudents(query);
    }

    @PreAuthorize("hasAuthority('student:insert') and hasAuthority('user:insert') and hasAuthority('user_role:insert')")
    @PostMapping("/adds")
    public <T> BaseResponse<T> addStudents(@RequestBody @Valid ValidateList<Enrollment> enrollments) {
        return studentService.importStudent(enrollments);
    }

    @PreAuthorize("hasAuthority('student:delete') and hasAuthority('user:delete') and hasAuthority('user_role:delete')")
    @DeleteMapping("/delete/{studentId}")
    public <T> BaseResponse<T> deleteStudent(@PathVariable String studentId) {
        return studentService.deleteStudent(studentId);
    }

    @PreAuthorize("hasAuthority('student:update') and hasAuthority('user:update:all')")
    @PutMapping("/recovery/{studentId}")
    public <T> BaseResponse<T> recoveryStudent(@PathVariable String studentId) {
        return studentService.recoveryStudent(studentId);
    }

    @PreAuthorize("hasAuthority('student:update')")
    @PutMapping("/update")
    public <T> BaseResponse<T> updateStudent(@RequestBody @Valid Enrollment enrollment) {
        return studentService.updateStudent(enrollment);
    }

    @PreAuthorize("hasAuthority('student:status') or hasAuthority('student:status:all')")
    @PostMapping("/stat")
    public BaseResponse<List<StudentStatItem>> getAllStudents(@RequestBody StudentStatQuery query) {
        return studentService.getStudentStatus(query);
    }

    @PreAuthorize("hasAuthority('user:select') and hasAuthority('user_role:select') and hasAuthority('role:select')")
    @GetMapping("/gets/headerTeacher")
    public BaseResponse<List<HeaderTeacher>> getAllHeaderTeachers() {
        return studentService.getHeaderTeachers();
    }

    @PreAuthorize("hasAuthority('student_competition:select:own')")
    @GetMapping("/basic/get/{studentId}")
    public BaseResponse<List<StudentBasicItem>> getStudent(@PathVariable String studentId) {
        return studentService.getStudentBasic(studentId);
    }

    @PreAuthorize("hasAuthority('student_competition:select:own')")
    @PostMapping("/basic/gets")
    public BaseResponse<List<StudentBasicItem>> getStudentBasics(@RequestBody List<String> studentIds) {
        return studentService.getStudentCompetitionTeam(studentIds);
    }

    @PreAuthorize("hasAuthority('student:select:student')")
    @GetMapping("/get/{studentId}")
    public BaseResponse<EnrollmentItem> getStudentEnrollment(@PathVariable String studentId) {
        return studentService.getOwnEnrollment(studentId);
    }
}
