package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.employment.*;
import edu.guet.studentworkmanagementsystem.entity.po.employment.StudentEmployment;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.StudentEmploymentStatGroup;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.StudentEmploymentItem;
import edu.guet.studentworkmanagementsystem.service.employment.EmploymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employment")
public class EmploymentController {
    @Autowired
    private EmploymentService employmentService;

    @PreAuthorize("hasAuthority('student_employment:select')")
    @PostMapping("/gets")
    public BaseResponse<Page<StudentEmploymentItem>> getStudentEmployment(@RequestBody EmploymentQuery query) {
        return employmentService.getStudentEmployment(query);
    }

    @PreAuthorize("hasAuthority('student_employment:insert')")
    @PostMapping("/adds")
    public <T> BaseResponse<T> importStudentEmployment(@RequestBody @Validated({InsertGroup.class}) ValidateList<StudentEmployment> studentEmployments ) {
        return employmentService.importStudentEmployment(studentEmployments);
    }

    @PreAuthorize("hasAuthority('student_employment:update')")
    @PutMapping("/update")
    public <T> BaseResponse<T> updateStudentEmployment(@RequestBody @Validated({UpdateGroup.class}) StudentEmployment StudentEmployment) {
        return employmentService.updateStudentEmployment(StudentEmployment);
    }

    @PreAuthorize("hasAuthority('student_employment:delete')")
    @DeleteMapping("/delete/{studentEmploymentId}")
    public <T> BaseResponse<T> deleteStudentEmployment(@PathVariable String studentEmploymentId) {
        return employmentService.deleteStudentEmployment(studentEmploymentId);
    }

    @PreAuthorize("hasAuthority('student_employment:select')")
    @PostMapping("/stat")
    public BaseResponse<List<StudentEmploymentStatGroup>> getStat(@RequestBody EmploymentStatQuery query) {
        return employmentService.getStat(query);
    }
}
