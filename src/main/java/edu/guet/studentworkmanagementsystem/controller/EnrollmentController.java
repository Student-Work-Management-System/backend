package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentList;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollmentInfo.EnrollmentQuery;
import edu.guet.studentworkmanagementsystem.entity.po.enrollment.Enrollment;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.service.enrollment.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
}
