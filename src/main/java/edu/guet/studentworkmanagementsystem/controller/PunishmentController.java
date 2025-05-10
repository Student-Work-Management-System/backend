package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.*;
import edu.guet.studentworkmanagementsystem.entity.dto.punishment.StudentPunishmentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.punishment.StudentPunishmentStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.punishment.Punishment;
import edu.guet.studentworkmanagementsystem.entity.po.punishment.StudentPunishment;
import edu.guet.studentworkmanagementsystem.entity.vo.punishment.StudentPunishmentItem;
import edu.guet.studentworkmanagementsystem.entity.vo.punishment.StudentPunishmentStatGroup;
import edu.guet.studentworkmanagementsystem.service.punlishment.PunishmentService;
import edu.guet.studentworkmanagementsystem.service.punlishment.StudentPunishmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/punishment")
public class PunishmentController {
    @Autowired
    private StudentPunishmentService studentPunishmentService;

    @Autowired
    private PunishmentService punishmentService;

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

    @PreAuthorize("hasAuthority('student_punishment:select')")
    @PostMapping("/stat")
    public BaseResponse<List<StudentPunishmentStatGroup>> getStat(@RequestBody StudentPunishmentStatQuery query) {
        return studentPunishmentService.getStat(query);
    }

    @PreAuthorize("hasAuthority('punishment:select')")
    @PostMapping("/gets")
    public BaseResponse<Page<Punishment>> getPunishments(@RequestBody BaseQuery query) {
        return punishmentService.getPunishments(query);
    }

    @PreAuthorize("hasAuthority('punishment:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> addPunishmentItem(@RequestBody @Validated({InsertGroup.class}) Punishment punishment) {
        return punishmentService.addPunishmentItem(punishment);
    }

    @PreAuthorize("hasAuthority('punishment:update')")
    @PutMapping("/update")
    public <T> BaseResponse<T> updatePunishmentItem(@RequestBody @Validated({UpdateGroup.class}) Punishment punishment) {
        return punishmentService.updatePunishmentItem(punishment);
    }

    @PreAuthorize("hasAuthority('punishment:delete')")
    @DeleteMapping("/delete/{punishmentId}")
    public <T> BaseResponse<T> deletePunishmentItem(@PathVariable String punishmentId) {
        return punishmentService.deletePunishmentItem(punishmentId);
    }
}
