package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.employment.EmploymentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.employment.InsertEmploymentDTOList;
import edu.guet.studentworkmanagementsystem.entity.dto.employment.InsertStudentEmploymentDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.employment.UpdateStudentEmploymentDTO;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.StudentEmploymentVO;
import edu.guet.studentworkmanagementsystem.service.employment.EmploymentService;
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
}
