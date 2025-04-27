package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.*;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.StudentScholarship;
import edu.guet.studentworkmanagementsystem.entity.vo.scholarship.StudentScholarshipItem;
import edu.guet.studentworkmanagementsystem.entity.vo.scholarship.StudentScholarshipStatGroup;
import edu.guet.studentworkmanagementsystem.service.scholarship.ScholarshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scholarship")
public class ScholarshipController {
    @Autowired
    private ScholarshipService scholarshipService;

    @PreAuthorize("hasAuthority('scholarship:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> addScholarship(@RequestBody @Validated({InsertGroup.class}) Scholarship scholarship) {
        return scholarshipService.insertScholarship(scholarship);
    }

    @PreAuthorize("hasAuthority('scholarship:update')")
    @PutMapping("/update")
    public <T> BaseResponse<T> updateScholarship(@RequestBody @Validated({UpdateGroup.class}) Scholarship scholarship) {
        return scholarshipService.updateScholarship(scholarship);
    }

    @PreAuthorize("hasAuthority('scholarship:delete')")
    @DeleteMapping("/delete/{scholarshipId}")
    public <T> BaseResponse<T> deleteScholarship(@PathVariable String scholarshipId) {
        return scholarshipService.deleteScholarship(scholarshipId);
    }

    @PreAuthorize("hasAuthority('scholarship:select')")
    @GetMapping("/gets")
    public BaseResponse<List<Scholarship>> getScholarships() {
        return scholarshipService.getScholarships();
    }

    @PreAuthorize("hasAuthority('student_scholarship:select')")
    @PostMapping("/student/gets")
    public BaseResponse<Page<StudentScholarshipItem>> getStudentScholarship(@RequestBody ScholarshipQuery query) {
        return scholarshipService.getStudentScholarship(query);
    }

    @PreAuthorize("hasAuthority('student_scholarship:insert')")
    @PostMapping("/student/add")
    public <T> BaseResponse<T> insertStudentScholarship(@RequestBody @Validated({InsertGroup.class}) ValidateList<StudentScholarship> studentScholarship) {
        return scholarshipService.insertStudentScholarship(studentScholarship);
    }

    @PreAuthorize("hasAuthority('student_scholarship:update')")
    @PutMapping("/student/update")
    public <T> BaseResponse<T> updateStudentScholarshipInfo(@RequestBody @Validated({UpdateGroup.class}) StudentScholarship studentScholarship) {
        return scholarshipService.updateStudentScholarship(studentScholarship);
    }

    @PreAuthorize("hasAuthority('student_scholarship:delete')")
    @DeleteMapping("/student/delete/{studentScholarshipId}")
    public <T> BaseResponse<T> deleteStudentScholarship(@PathVariable String studentScholarshipId) {
        return scholarshipService.deleteStudentScholarship(studentScholarshipId);
    }

    @PreAuthorize("hasAuthority('studnet_scholarship:select') and hasAuthority('scholarship:select')")
    @PostMapping("/stat")
    public BaseResponse<List<StudentScholarshipStatGroup>> getStat(@RequestBody ScholarshipStatQuery query) {
        return scholarshipService.getStat(query);
    }
}
