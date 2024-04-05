package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.employment.EmploymentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.employment.StudentEmploymentDTO;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.StudentEmploymentVO;
import edu.guet.studentworkmanagementsystem.service.employment.EmploymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employment")
public class EmploymentController {

    @Autowired
    private EmploymentService employmentService;

    @PreAuthorize("hasAuthority('student_employment:select')")
    @PostMapping("/gets")
    public BaseResponse<Page<StudentEmploymentVO>> getStudentEmployment(@RequestBody EmploymentQuery query) {
        return employmentService.getStudentEmployment(query);
    }

    @PreAuthorize("hasAuthority('student_employment:insert')")
    @PostMapping("/adds")
    public <T> BaseResponse<T> importStudentEmployment(@RequestBody List<StudentEmploymentDTO> studentEmploymentDTOList) {
        return employmentService.importStudentEmployment(studentEmploymentDTOList);
    }

    @PreAuthorize("hasAuthority('student_employment:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> insertStudentEmployment(@RequestBody @Valid StudentEmploymentDTO studentEmploymentDTO) {
        return employmentService.insertStudentEmployment(studentEmploymentDTO);
    }

    @PreAuthorize("hasAuthority('student_employment:update')")
    @PutMapping("/update")
    public <T> BaseResponse<T> updateStudentEmployment(@RequestBody @Valid StudentEmploymentDTO studentEmploymentDTO) {
        return employmentService.updateStudentEmployment(studentEmploymentDTO);
    }

    @PreAuthorize("hasAuthority('student_employment:delete')")
    @DeleteMapping("/delete/{studentId}")
    public <T> BaseResponse<T> deleteStudentEmployment(@PathVariable String studentId) {
        return employmentService.deleteStudentEmployment(studentId);
    }
}
