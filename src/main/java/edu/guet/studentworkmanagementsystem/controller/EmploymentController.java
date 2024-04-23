package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.employment.*;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.StudentEmploymentVO;
import edu.guet.studentworkmanagementsystem.service.employment.EmploymentService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employment")
public class EmploymentController {
    @Autowired
    private EmploymentService employmentService;
    @PreAuthorize("hasAuthority('student_employment:select') and hasAnyAuthority('student:select') and hasAnyAuthority('major:select')")
    @PostMapping("/gets")
    public BaseResponse<Page<StudentEmploymentVO>> getStudentEmployment(@RequestBody EmploymentQuery query) {
        return employmentService.getStudentEmployment(query);
    }
    @PreAuthorize("hasAuthority('student_employment:insert')")
    @PostMapping("/adds")
    public <T> BaseResponse<T> importStudentEmployment(@RequestBody @Valid InsertEmploymentDTOList insertEmploymentDTOList) {
        return employmentService.importStudentEmployment(insertEmploymentDTOList);
    }
    @PreAuthorize("hasAuthority('student_employment:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> insertStudentEmployment(@RequestBody @Valid InsertStudentEmploymentDTO insertStudentEmploymentDTO) {
        return employmentService.insertStudentEmployment(insertStudentEmploymentDTO);
    }
    @PreAuthorize("hasAuthority('student_employment:update')")
    @PutMapping("/update")
    public <T> BaseResponse<T> updateStudentEmployment(@RequestBody @Valid UpdateStudentEmploymentDTO updateStudentEmploymentDTO) {
        return employmentService.updateStudentEmployment(updateStudentEmploymentDTO);
    }
    @PreAuthorize("hasAuthority('student_employment:delete')")
    @DeleteMapping("/delete/{studentEmploymentId}")
    public <T> BaseResponse<T> deleteStudentEmployment(@PathVariable String studentEmploymentId) {
        return employmentService.deleteStudentEmployment(studentEmploymentId);
    }
    @PreAuthorize("hasAuthority('student_employment:select')")
    @PostMapping("/download")
    public void download(@RequestBody EmploymentStatQuery query, HttpServletResponse response) {
        employmentService.download(query, response);
    }
    @PreAuthorize("hasAuthority('student:employment:select')")
    @PostMapping("/statistics")
    public <T> BaseResponse<T> statistics(@RequestBody EmploymentStatQuery query) {
        return employmentService.statistics(query);
    }
}
