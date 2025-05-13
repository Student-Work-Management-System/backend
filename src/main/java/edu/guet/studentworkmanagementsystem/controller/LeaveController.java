package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.*;
import edu.guet.studentworkmanagementsystem.entity.vo.leave.StudentLeaveItem;
import edu.guet.studentworkmanagementsystem.entity.vo.leave.StudentLeaveStatGroup;
import edu.guet.studentworkmanagementsystem.service.leave.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave")
public class LeaveController {
    @Autowired
    private LeaveService leaveService;

    @PreAuthorize("hasAuthority('student_leave:insert') and hasAuthority('student_leave_audit:insert')")
    @PostMapping("/student/add")
    public <T> BaseResponse<T> addLeave(@RequestBody StudentLeaveRequest request){
        return leaveService.addStudentLeave(request);
    }

    @PreAuthorize("hasAuthority('student_leave:select:student') and hasAuthority('student_leave_audit:select:student')")
    @PostMapping("/student")
    public BaseResponse<Page<StudentLeaveItem>> getOwn(@RequestBody StudentLeaveQuery query) {
        return leaveService.getStudentOnwRecord(query);
    }

    @PreAuthorize("hasAuthority('student_leave:select') and hasAuthority('student_leave_audit:select')")
    @PostMapping("/gets")
    public BaseResponse<Page<StudentLeaveItem>> getAuditRecord(@RequestBody AuditLeaveQuery query) {
        return leaveService.getAuditRecord(query);
    }

    @PreAuthorize("hasAuthority('student_leave_audit:update:student')")
    @PutMapping("/student/destroy/{leaveId}")
    public BaseResponse<StudentLeaveItem> destroy(@PathVariable String leaveId) {
        return leaveService.studentDestroyLeave(leaveId);
    }

    @PreAuthorize("hasAuthority('student_leave_audit:update:counselor')")
    @PutMapping("/counselor/audit")
    public <T> BaseResponse<T> counselorAudit(@RequestBody AuditOperator operator) {
        return leaveService.counselorAudit(operator);
    }

    @PreAuthorize("hasAuthority('student_leave_audit:update:leader')")
    @PutMapping("/leader/audit")
    public <T> BaseResponse<T> leaderAudit(@RequestBody AuditOperator operator) {
        return leaveService.leaderAudit(operator);
    }

    @PreAuthorize("hasAuthority('student_leave:select')")
    @PostMapping("/stat")
    public BaseResponse<List<StudentLeaveStatGroup>> getStat(@RequestBody LeaveStatQuery query) {
        return leaveService.getStat(query);
    }
}
