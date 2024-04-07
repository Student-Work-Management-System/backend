package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.punishment.PunishmentList;
import edu.guet.studentworkmanagementsystem.entity.dto.punishment.PunishmentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.punishment.StudentPunishmentDTO;
import edu.guet.studentworkmanagementsystem.entity.po.punishment.StudentPunishment;
import edu.guet.studentworkmanagementsystem.entity.vo.punishment.StudentPunishmentVO;
import edu.guet.studentworkmanagementsystem.service.punlishment.PunishmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/punishment")
public class PunishmentController {
    @Autowired
    private PunishmentService punishmentService;
    @PreAuthorize("hasAuthority('student_punishment:select') and hasAuthority('student:select') and hasAuthority('major:select')")
    @PostMapping("/gets")
    public BaseResponse<Page<StudentPunishmentVO>> getAllStudentPunishment(@RequestBody PunishmentQuery query) {
        return punishmentService.getAllStudentPunishment(query);
    }
    @PreAuthorize("hasAuthority('student_punishment:insert')")
    @PostMapping("/adds")
    public <T> BaseResponse<T> importStudentPunishment(@RequestBody @Valid PunishmentList punishmentList) {
        return punishmentService.importStudentPunishment(punishmentList);
    }
    @PreAuthorize("hasAuthority('student_punishment:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> insertStudentPunishment(@RequestBody @Valid StudentPunishment studentPunishment) {
        return punishmentService.insertStudentPunishment(studentPunishment);
    }
    @PreAuthorize("hasAuthority('student_punishment:update')")
    @PutMapping("/update")
    public <T> BaseResponse<T> updateStudentPunishment(@RequestBody @Valid StudentPunishmentDTO studentPunishmentDTO) {
        return punishmentService.updateStudentPunishment(studentPunishmentDTO);
    }
    @PreAuthorize("hasAuthority('student_punishment:delete')")
    @DeleteMapping("/delete/{studentPunishmentId}")
    public <T> BaseResponse<T> deleteStudentPunishment(@PathVariable String studentPunishmentId) {
        return punishmentService.deleteStudentPunishment(studentPunishmentId);
    }
}
