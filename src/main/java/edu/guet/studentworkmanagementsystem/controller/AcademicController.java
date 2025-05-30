package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkRequest;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.AcademicWorkAudit;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.AcademicWorkStatGroup;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.AcademicWorkUser;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.AcademicWorkItem;
import edu.guet.studentworkmanagementsystem.service.academicWork.AcademicWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/academicWork")
public class AcademicController {
    @Autowired
    private AcademicWorkService academicWorkService;

    @PreAuthorize("hasAuthority('student_academic_work:select:own')")
    @GetMapping("/get/{studentId}")
    public BaseResponse<List<AcademicWorkItem>> getOwnAcademicWork(@PathVariable String studentId) {
        return academicWorkService.getOwnAcademicWork(studentId);
    }

    @PreAuthorize("hasAuthority('student_academic_work:select')")
    @PostMapping("/gets")
    public BaseResponse<Page<AcademicWorkItem>> getAllAcademicWork(@RequestBody AcademicWorkQuery query) {
        return academicWorkService.getAllAcademicWork(query);
    }

    @PreAuthorize("hasAuthority('student_academic_work:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> insertAcademicWork(@RequestBody @Validated({InsertGroup.class}) AcademicWorkRequest academicWorkRequest) {
        return academicWorkService.insertAcademicWork(academicWorkRequest);
    }

    @PreAuthorize("hasAuthority('student_academic_work:update')")
    @PutMapping("/update")
    public <T> BaseResponse<T> updateAcademicWork(@RequestBody @Validated({UpdateGroup.class}) ValidateList<AcademicWorkAudit> audits) {
        return academicWorkService.updateAcademicWorkAudit(audits);
    }

    @PreAuthorize("hasAuthority('student_academic_work:delete')")
    @DeleteMapping("/delete/{studentAcademicWorkId}")
    public <T> BaseResponse<T> deleteAcademicWork(@PathVariable String studentAcademicWorkId) {
        return academicWorkService.deleteAcademicWork(studentAcademicWorkId);
    }

    @PreAuthorize("hasAuthority('student_academic_work:select:own') or hasAuthority('student_academic_work:select')")
    @GetMapping("/user/{username}")
    public BaseResponse<List<AcademicWorkUser>> getOptionalUserByUsername(@PathVariable String username) {
        return academicWorkService.getOptionalUserByUsername(username);
    }

    @PreAuthorize("hasAuthority('student_academic_work:select')")
    @GetMapping("/stat")
    public BaseResponse<AcademicWorkStatGroup> getStat() {
        return academicWorkService.getStat();
    }
}
