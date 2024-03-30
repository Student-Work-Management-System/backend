package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.status.StatusQuery;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.entity.po.status.StudentStatus;
import edu.guet.studentworkmanagementsystem.entity.vo.status.StudentStatusVO;
import edu.guet.studentworkmanagementsystem.service.status.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/status")
public class StatusController {
    @Autowired
    private StatusService statusService;
    @PreAuthorize("hasAuthority('student_status:insert')")
    @PostMapping("/adds")
    public <T> BaseResponse<T> importStudentStatus(@RequestBody List<StudentStatus> studentStatuses) {
        return statusService.importStudentStatus(studentStatuses);
    }
    @PreAuthorize("hasAuthority('student_status:insert')")
    @PostMapping("/add")
    public BaseResponse<Scholarship> insertStudentStatus(@RequestBody StudentStatus studentStatus) {
        return statusService.insertStudentStatus(studentStatus);
    }
    @PreAuthorize("hasAuthority('student_status:select') and hasAuthority('student:select')")
    @PostMapping("/gets")
    public BaseResponse<Page<StudentStatusVO>> getAllRecords(@RequestBody StatusQuery query) {
        return statusService.getAllRecords(query);
    }
    @PreAuthorize("hasAuthority('student_status:update')")
    @PutMapping("/update")
    public  <T> BaseResponse<T> updateStudentStatus(@RequestBody StudentStatus studentStatus) {
        return statusService.updateStudentStatus(studentStatus);
    }
    @PreAuthorize("hasAuthority('student_status:delete')")
    @DeleteMapping("/delete/{studentStatusId}")
    public <T> BaseResponse<T> deleteStudentStatus(@PathVariable String studentStatusId) {
        return statusService.deleteStudentStatus(studentStatusId);
    }
}
