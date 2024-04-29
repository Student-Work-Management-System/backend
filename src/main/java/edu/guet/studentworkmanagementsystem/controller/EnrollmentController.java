package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentList;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.enrollment.Enrollment;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.entity.vo.enrollment.EnrollmentStatistics;
import edu.guet.studentworkmanagementsystem.service.enrollment.EnrollmentService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;
    @PreAuthorize("hasAuthority('enrollment:insert')")
    @PostMapping("/adds")
    public <T> BaseResponse<T> importEnrollment(@RequestBody @Validated({InsertGroup.class}) EnrollmentList enrollmentList) {
        return enrollmentService.importEnrollment(enrollmentList);
    }
    @PreAuthorize("hasAuthority('enrollment:insert')")
    @PostMapping("/add")
    public BaseResponse<Scholarship> insertEnrollment(@RequestBody @Validated({InsertGroup.class}) Enrollment enrollment) {
        return enrollmentService.insertEnrollment(enrollment);
    }
    @PreAuthorize("hasAuthority('enrollment:update')")
    @PutMapping("/update")
    public  <T> BaseResponse<T> updateEnrollment(@RequestBody @Validated({UpdateGroup.class}) Enrollment enrollment) {
        return enrollmentService.updateEnrollment(enrollment);
    }
    @PreAuthorize("hasAuthority('enrollment:delete')")
    @DeleteMapping("/delete/{enrollmentId}")
    public <T> BaseResponse<T> deleteEnrollment(@PathVariable String enrollmentId) {
        return enrollmentService.deleteEnrollment(enrollmentId);
    }
    @PreAuthorize("hasAuthority('enrollment:select')")
    @PostMapping("/gets")
    public BaseResponse<Page<Enrollment>> getAllRecords(@RequestBody EnrollmentQuery query) {
        return enrollmentService.getAllRecords(query);
    }
    @PreAuthorize("hasAuthority('enrollment:select')")
    @PostMapping("/download")
    public void download(@RequestBody EnrollmentStatQuery query, HttpServletResponse response) {
        enrollmentService.download(query, response);
    }
    @PreAuthorize("hasAuthority('enrollment:select')")
    @PostMapping("/stat")
    public BaseResponse<HashMap<String, EnrollmentStatistics>> statistics(@RequestBody EnrollmentStatQuery query) {
        return enrollmentService.statistics(query);
    }
}
