package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentList;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentQuery;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentVO;
import edu.guet.studentworkmanagementsystem.service.student.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @PreAuthorize("hasAuthority('student:select')")
    @PostMapping("/gets")
    public BaseResponse<Page<StudentVO>> getAllStudent(@RequestBody StudentQuery query) {
        return studentService.getStudents(query);
    }
    @PreAuthorize("hasAuthority('student:insert') and hasAuthority('user:insert') and hasAuthority('user_role:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> addStudent(@RequestBody @Valid Student student) {
        return studentService.addStudent(student);
    }
    @PreAuthorize("hasAuthority('student:insert') and hasAuthority('user:insert') and hasAuthority('user_role:insert')")
    @PostMapping("/adds")
    public <T> BaseResponse<T> addStudents(@RequestBody @Valid StudentList studentList) {
        return studentService.importStudent(studentList);
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
    public <T> BaseResponse<T> updateStudent(@RequestBody @Valid StudentDTO studentDTO) {
        return studentService.updateStudent(studentDTO);
    }
}
