package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.ScholarshipQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.StudentScholarshipDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.UpdateScholarshipOwner;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.UpdateStudentScholarshipType;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.entity.vo.scholarship.StudentScholarshipVO;
import edu.guet.studentworkmanagementsystem.service.scholarship.ScholarshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ScholarshipController {
    @Autowired
    private ScholarshipService scholarshipService;
    @PreAuthorize("hasAuthority('scholarship:insert')")
    @PostMapping("/scholarship/add")
    public <T> BaseResponse<T> addScholarship(@RequestBody Scholarship scholarship) {
        return scholarshipService.insertScholarship(scholarship);
    }
    @PreAuthorize("hasAuthority('scholarship:insert')")
    @PostMapping("/scholarship/adds")
    public <T> BaseResponse<T> addScholarships(@RequestBody List<Scholarship> scholarships) {
        return scholarshipService.importScholarship(scholarships);
    }
    @PreAuthorize("hasAuthority('scholarship:update')")
    @PutMapping("/scholarship/update")
    public <T> BaseResponse<T> updateScholarship(@RequestBody Scholarship scholarship) {
        return scholarshipService.updateScholarship(scholarship);
    }
    @PreAuthorize("hasAuthority('scholarship:delete')")
    @DeleteMapping("/scholarship/delete/{scholarshipId}")
    public <T> BaseResponse<T> deleteScholarship(@PathVariable String scholarshipId) {
        return scholarshipService.deleteScholarship(scholarshipId);
    }
    @PreAuthorize("hasAuthority('scholarship:select')")
    @GetMapping("/scholarship/gets")
    public BaseResponse<List<Scholarship>> getScholarships() {
        return scholarshipService.getScholarships();
    }
    @PreAuthorize(
            "hasAuthority('scholarship:select')" +
            " and hasAuthority('student_scholarship:select')" +
            " and hasAuthority('student:select')" +
            " and hasAuthority('major:select')"
    )
    @GetMapping("/student_scholarship/gets")
    public BaseResponse<Page<StudentScholarshipVO>> getStudentScholarship(@RequestBody ScholarshipQuery query) {
        return scholarshipService.getStudentScholarship(query);
    }
    @PreAuthorize("hasAuthority('student_scholarship:insert')")
    @PostMapping("/student_scholarship/add")
    public <T> BaseResponse<T> arrangeStudentScholarship(@RequestBody StudentScholarshipDTO studentScholarshipDTO) {
        return scholarshipService.arrangeStudentScholarship(studentScholarshipDTO);
    }
    @PreAuthorize("hasAuthority('student_scholarship:update')")
    @PutMapping("/student_scholarship/update")
    public <T> BaseResponse<T> updateStudentScholarshipInfo(@RequestBody StudentScholarshipDTO studentScholarshipDTO) {
        return scholarshipService.updateStudentScholarshipInfo(studentScholarshipDTO);
    }
    @PreAuthorize("hasAuthority('student_scholarship:update')")
    @PutMapping("/student_scholarship/update/type")
    public <T> BaseResponse<T> updateStudentScholarshipType(@RequestBody UpdateStudentScholarshipType updateStudentScholarshipType) {
        return scholarshipService.updateStudentScholarshipType(updateStudentScholarshipType);
    }
    @PreAuthorize("hasAuthority('student_scholarship:update')")
    @PutMapping("/student_scholarship/update/owner")
    public <T> BaseResponse<T> updateStudentScholarshipOwner(@RequestBody UpdateScholarshipOwner updateScholarshipOwner) {
        return scholarshipService.updateStudentScholarshipOwner(updateScholarshipOwner);
    }
    @PreAuthorize("hasAuthority('student_scholarship:delete')")
    @DeleteMapping("/student_scholarship/delete/{studentScholarshipId}")
    public  <T> BaseResponse<T> deleteStudentScholarship(@PathVariable String studentScholarshipId) {
        return scholarshipService.deleteStudentScholarship(studentScholarshipId);
    }
}
