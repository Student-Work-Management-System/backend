package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.LeaveList;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.LeaveQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.StudentLeaveAuditDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.StudentLeaveDTO;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeave;
import edu.guet.studentworkmanagementsystem.entity.vo.leave.StudentLeaveItem;
import edu.guet.studentworkmanagementsystem.service.leave.LeaveService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/leave")
public class LeaveController {
    @Autowired
    private LeaveService leaveService;
    @PreAuthorize(
            "hasAuthority('student:select') " +
            "and hasAuthority('student_leave:select') " +
            "and hasAuthority('student_leave_audit:select') " +
            "and hasAuthority('major:select') " +
            "and hasAuthority('user:select')"
    )
    @PostMapping("/gets")
    public BaseResponse<Page<StudentLeaveItem>> getStudentLeave(@RequestBody LeaveQuery query) {
        return leaveService.getStudentLeave(query);
    }
    @PreAuthorize("hasAuthority('student_leave:insert') and hasAuthority('student_leave_audit:insert')")
    @PostMapping("/adds")
    public <T> BaseResponse<T> importStudentLeave(@RequestBody @Validated({InsertGroup.class}) LeaveList studentLeaves) {
        return leaveService.importStudentLeave(studentLeaves);
    }
    @PreAuthorize("hasAuthority('student_leave:insert') and hasAuthority('student_leave_audit:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> insertStudentLeave(@RequestBody @Validated({InsertGroup.class}) StudentLeave studentLeave) {
        return leaveService.insertStudentLeave(studentLeave);
    }
    @PreAuthorize("hasAuthority('student_leave_audit:update')")
    @PostMapping("/audit")
    public <T> BaseResponse<T> auditStudentLeave(@RequestBody @Valid StudentLeaveAuditDTO studentLeaveAuditDTO) {
        return leaveService.audiStudentLeave(studentLeaveAuditDTO);
    }
    @PreAuthorize("hasAuthority('student_leave:update')")
    @PutMapping("/update")
    public <T> BaseResponse<T> updateStudentLeave(@RequestBody @Valid StudentLeaveDTO studentLeaveDTO) {
        return leaveService.updateStudentLeave(studentLeaveDTO);
    }
    @PreAuthorize("hasAuthority('student_leave:delete') and hasAuthority('student:leave_audit:delete')")
    @DeleteMapping("/delete/{studentLeaveId}")
    public <T> BaseResponse<T> deleteStudentLeave(@PathVariable String studentLeaveId) {
        return leaveService.deleteStudentLeave(studentLeaveId);
    }
}
