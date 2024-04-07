package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentInfoList;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollmentInfo.EnrollmentQuery;
import edu.guet.studentworkmanagementsystem.entity.po.enrollment.EnrollmentInfo;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.service.enrollmentInfo.EnrollmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enrollment")
public class EnrollInfoController {
    @Autowired
    private EnrollmentInfoService enrollInfoService;
    @PreAuthorize("hasAuthority('enrollment_info:insert')")
    @PostMapping("/adds")
    public <T> BaseResponse<T> importEnrollmentInfo(@RequestBody @Validated({InsertGroup.class}) EnrollmentInfoList enrollmentInfos) {
        return enrollInfoService.importEnrollmentInfo(enrollmentInfos);
    }
    @PreAuthorize("hasAuthority('enrollment_info:insert')")
    @PostMapping("/add")
    public BaseResponse<Scholarship> insertEnrollmentInfo(@RequestBody @Validated({InsertGroup.class}) EnrollmentInfo enrollmentInfo) {
        return enrollInfoService.insertEnrollmentInfo(enrollmentInfo);
    }
    @PreAuthorize("hasAuthority('enrollment_info:update')")
    @PutMapping("/update")
    public  <T> BaseResponse<T> updateEnrollmentInfo(@RequestBody @Validated({UpdateGroup.class}) EnrollmentInfo enrollmentInfo) {
        return enrollInfoService.updateEnrollmentInfo(enrollmentInfo);
    }
    @PreAuthorize("hasAuthority('enrollment_info:delete')")
    @DeleteMapping("/delete/{enrollmentInfoId}")
    public <T> BaseResponse<T> deleteEnrollmentInfo(@PathVariable String enrollmentInfoId) {
        return enrollInfoService.deleteEnrollmentInfo(enrollmentInfoId);
    }
    @PreAuthorize("hasAuthority('enrollment_info:select')")
    @PostMapping("/gets")
    public BaseResponse<Page<EnrollmentInfo>> getAllRecords(@RequestBody EnrollmentQuery query) {
        return enrollInfoService.getAllRecords(query);
    }
}
