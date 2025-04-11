package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkRequest;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.StudentAcademicWorkAudit;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.AcademicWorkUser;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.StudentAcademicWorkItem;
import edu.guet.studentworkmanagementsystem.service.academicWork.AcademicWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/academic_work")
public class AcademicController {
    @Autowired
    private AcademicWorkService academicWorkService;

    @PreAuthorize("hasAuthority('student_academic_work:select:own')")
    @GetMapping("/get/{studentId}")
    public BaseResponse<List<StudentAcademicWorkItem>> getOwnStudentAcademicWork(@PathVariable String studentId) {
        return academicWorkService.getOwnStudentAcademicWork(studentId);
    }

    @PreAuthorize("hasAuthority('student_academic_work:select')")
    @PostMapping("/gets")
    public BaseResponse<Page<StudentAcademicWorkItem>> getAllStudentAcademicWork(@RequestBody AcademicWorkQuery query) {
        return academicWorkService.getAllStudentAcademicWork(query);
    }

    @PreAuthorize("hasAuthority('student_academic_work:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> insertStudentAcademicWork(@RequestBody @Validated({InsertGroup.class}) AcademicWorkRequest academicWorkRequest) {
        return academicWorkService.insertStudentAcademicWork(academicWorkRequest);
    }

    @PreAuthorize("hasAuthority('student_academic_work:update')")
    @PutMapping("/update")
    public <T> BaseResponse<T> updateStudentAcademicWork(@RequestBody @Validated({UpdateGroup.class}) StudentAcademicWorkAudit audit) {
        return academicWorkService.updateStudentAcademicWorkAudit(audit);
    }

    @PreAuthorize("hasAuthority('student_academic_work:delete')")
    @DeleteMapping("/delete/{studentAcademicWorkId}")
    public <T> BaseResponse<T> deleteStudentAcademicWork(@PathVariable String studentAcademicWorkId) {
        return academicWorkService.deleteStudentAcademicWork(studentAcademicWorkId);
    }

    @PreAuthorize("hasAuthority('student_academic_work:select:own') or hasAuthority('student_academic_work:select')")
    @GetMapping("/user/{username}")
    public BaseResponse<List<AcademicWorkUser>> getOptionalUserByUsername(@PathVariable String username) {
        return academicWorkService.getOptionalUserByUsername(username);
    }
}
