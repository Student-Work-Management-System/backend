package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.punishment.StudentPunishmentQuery;
import edu.guet.studentworkmanagementsystem.entity.po.punishment.StudentPunishment;
import edu.guet.studentworkmanagementsystem.entity.vo.punishment.StudentPunishmentItem;
import edu.guet.studentworkmanagementsystem.service.punlishment.StudentPunishmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/punishment")
public class PunishmentController {
    @Autowired
    private StudentPunishmentService studentPunishmentService;

    @PreAuthorize("hasAuthority('student_punishment:select')")
    @PostMapping("/student/gets")
    public BaseResponse<Page<StudentPunishmentItem>> getStudentPunishments(@RequestBody StudentPunishmentQuery query) {
        return studentPunishmentService.getStudentPunishments(query);
    }

    @PreAuthorize("hasAuthority('student_punishment:insert')")
    @PostMapping("/student/adds")
    public <T> BaseResponse<T> importStudentPunishment(@RequestBody @Validated({InsertGroup.class}) ValidateList<StudentPunishment> studentPunishments) {
        return studentPunishmentService.importStudentPunishment(studentPunishments);
    }

    @PreAuthorize("hasAuthority('student_punishment:update')")
    @PutMapping("/student/update")
    public <T> BaseResponse<T> updateStudentPunishment(@RequestBody @Validated({UpdateGroup.class}) StudentPunishment studentPunishment) {
        return studentPunishmentService.updateStudentPunishment(studentPunishment);
    }

    @PreAuthorize("hasAuthority('student_punishment:delete')")
    @DeleteMapping("/student/delete/{studentPunishmentId}")
    public <T> BaseResponse<T> deleteStudentPunishment(@PathVariable String studentPunishmentId) {
        return studentPunishmentService.deleteStudentPunishment(studentPunishmentId);
    }
}
