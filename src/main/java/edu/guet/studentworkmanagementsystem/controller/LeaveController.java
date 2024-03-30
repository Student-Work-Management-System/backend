package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.LeaveQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.StudentLeaveAuditDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.StudentLeaveDTO;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeave;
import edu.guet.studentworkmanagementsystem.entity.vo.leave.StudentLeaveVO;
import edu.guet.studentworkmanagementsystem.service.leave.LeaveService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave")
public class LeaveController {
    @Autowired
    private LeaveService leaveService;
    @PostMapping("/gets")
    public BaseResponse<Page<StudentLeaveVO>> getAllRecord(@RequestBody LeaveQuery query) {
        return leaveService.getStudentLeave(query);
    }
    @PostMapping("/adds")
    public <T> BaseResponse<T> importStudentLeave(@RequestBody List<StudentLeave> studentLeaves) {
        return leaveService.importStudentLeave(studentLeaves);
    }
    @PostMapping("/add")
    public <T> BaseResponse<T> insertStudentLeave(@RequestBody StudentLeave studentLeave) {
        return leaveService.insertStudentLeave(studentLeave);
    }
    @PostMapping("/audit")
    public <T> BaseResponse<T> auditStudentLeave(@RequestBody @Valid StudentLeaveAuditDTO studentLeaveAuditDTO) {
        return leaveService.audiStudentLeave(studentLeaveAuditDTO);
    }
    @PutMapping("/update")
    public <T> BaseResponse<T> updateStudentLeave(@RequestBody @Valid StudentLeaveDTO studentLeaveDTO) {
        return leaveService.updateStudentLeave(studentLeaveDTO);
    }
    @DeleteMapping("/delete/{studentLeaveId}")
    public <T> BaseResponse<T> deleteStudentLeave(@PathVariable String studentLeaveId) {
        return leaveService.deleteStudentLeave(studentLeaveId);
    }
}
