package edu.guet.studentworkmanagementsystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkAuditDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.StudentAcademicWorkDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.StudentAcademicWorkList;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.StudentAcademicWorkVO;
import edu.guet.studentworkmanagementsystem.service.academicWork.AcademicWorkService;
import jakarta.validation.Valid;
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
    @PreAuthorize("hasAuthority('student_academic_work:select') and hasAuthority('student:select') and hasAuthority('academic:select')")
    @GetMapping("/get/{studentId}")
    public BaseResponse<List<StudentAcademicWorkVO>> getOwnStudentAcademicWork(@PathVariable String studentId) {
        return academicWorkService.getOwnStudentAcademicWork(studentId);
    }
    @PreAuthorize("hasAuthority('student_academic_work:select') and hasAuthority('student:select') and hasAuthority('academic:select')")
    @PostMapping("/gets")
    public BaseResponse<Page<StudentAcademicWorkVO>> getAllStudentAcademicWork(@RequestBody AcademicWorkQuery query) {
        return academicWorkService.getAllStudentAcademicWork(query);
    }
    @PreAuthorize("hasAuthority('student_academic_work:insert') and hasAuthority('academic:insert')")
    @PostMapping("/adds")
    public <T> BaseResponse<T> importStudentAcademicWork(@RequestBody @Valid StudentAcademicWorkList studentAcademicWorkList) {
        return academicWorkService.importStudentAcademicWork(studentAcademicWorkList);
    }
    @PreAuthorize("hasAuthority('student_academic_work:insert') and hasAuthority('academic:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> insertStudentAcademicWork(@RequestBody @Valid StudentAcademicWorkDTO studentAcademicWorkDTO) {
        return academicWorkService.insertStudentAcademicWork(studentAcademicWorkDTO);
    }
    @PreAuthorize("hasAuthority('student_academic_work:update') and hasAuthority('student_academic_work_claim:insert')")
    @PutMapping("/audit")
    public <T> BaseResponse<T> auditStudentAcademicWork(@RequestBody @Valid AcademicWorkAuditDTO academicWorkAuditDTO) throws JsonProcessingException {
        return academicWorkService.auditStudentAcademicWork(academicWorkAuditDTO);
    }
    @PreAuthorize("hasAuthority('student_academic_work:delete') and hasAuthority('student_academic_work_claim:delete') and hasAuthority('academic:delete')")
    @DeleteMapping("/delete/{studentAcademicWorkId}")
    public <T> BaseResponse<T> deleteStudentAcademicWork(@PathVariable String studentAcademicWorkId) {
        return academicWorkService.deleteStudentAcademicWork(studentAcademicWorkId);
    }
}
