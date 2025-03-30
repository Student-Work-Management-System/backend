package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.status.StudentStatusQuery;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.entity.po.status.Status;
import edu.guet.studentworkmanagementsystem.entity.po.status.StudentStatus;
import edu.guet.studentworkmanagementsystem.entity.vo.status.StudentStatusItem;
import edu.guet.studentworkmanagementsystem.service.status.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/status")
public class StatusController {
    @Autowired
    private StatusService statusService;

    @PreAuthorize("hasAuthority('student_status:select')")
    @GetMapping("/gets")
    public BaseResponse<List<Status>> getAllStatus() {
        return statusService.getAllStatus();
    }

    @PreAuthorize("hasAuthority('student_status:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> addStatus(@RequestBody @Validated({InsertGroup.class}) Status status) {
        return statusService.addStatus(status);
    }

    @PreAuthorize("hasAuthority('student_status:update')")
    @PutMapping("/update")
    public <T> BaseResponse<T> updateStatus(@RequestBody @Validated({UpdateGroup.class}) Status status) {
        return statusService.updateStatus(status);
    }

    @PreAuthorize("hasAuthority('student_status:delete')")
    @DeleteMapping("/delete/{statusId}")
    public <T> BaseResponse<T> deleteStatus(@PathVariable String statusId) {
        return statusService.deleteStatus(statusId);
    }

    @PreAuthorize("hasAuthority('student_status:insert')")
    @PostMapping("/student/adds")
    public <T> BaseResponse<T> importStudentStatus(@RequestBody @Validated({InsertGroup.class}) ValidateList<StudentStatus> studentStatuses) {
        return statusService.importStudentStatus(studentStatuses);
    }

    @PreAuthorize("hasAuthority('student_status:insert')")
    @PostMapping("/student/add")
    public BaseResponse<Scholarship> insertStudentStatus(@RequestBody @Validated({InsertGroup.class}) StudentStatus studentStatus) {
        return statusService.insertStudentStatus(studentStatus);
    }

    @PreAuthorize("hasAuthority('student_status:select')")
    @PostMapping("/student/gets")
    public BaseResponse<Page<StudentStatusItem>> getAllRecords(@RequestBody StudentStatusQuery query) {
        return statusService.getAllRecords(query);
    }

    @PreAuthorize("hasAuthority('student_status:update')")
    @PutMapping("/student/update")
    public  <T> BaseResponse<T> updateStudentStatus(@RequestBody @Validated({UpdateGroup.class}) StudentStatus studentStatus) {
        return statusService.updateStudentStatus(studentStatus);
    }

    @PreAuthorize("hasAuthority('student_status:delete')")
    @DeleteMapping("/student/delete/{studentStatusId}")
    public <T> BaseResponse<T> deleteStudentStatus(@PathVariable String studentStatusId) {
        return statusService.deleteStudentStatus(studentStatusId);
    }
}
