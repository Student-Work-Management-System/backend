package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance.*;
import edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.PovertyAssistance;
import edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.StudentPovertyAssistance;
import edu.guet.studentworkmanagementsystem.entity.vo.povertyAssistance.PovertyAssistanceStatGroup;
import edu.guet.studentworkmanagementsystem.entity.vo.povertyAssistance.StudentPovertyAssistanceItem;
import edu.guet.studentworkmanagementsystem.service.povertyAssistance.PovertyAssistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PovertyAssistanceController {
    @Autowired
    private PovertyAssistanceService povertyAssistanceService;

    @PreAuthorize("hasAuthority('poverty_assistance:select')")
    @GetMapping("/povertyAssistance/gets")
    public BaseResponse<List<PovertyAssistance>> getAllPovertyAssistance() {
        return povertyAssistanceService.getAllPovertyAssistance();
    }

    @PreAuthorize("hasAuthority('poverty_assistance:insert')")
    @PostMapping("/povertyAssistance/adds")
    public <T> BaseResponse<T> importPovertyAssistance(@RequestBody @Validated({InsertGroup.class}) ValidateList<PovertyAssistance> povertyAssistanceList) {
        return povertyAssistanceService.importPovertyAssistance(povertyAssistanceList);
    }

    @PreAuthorize("hasAuthority('poverty_assistance:insert')")
    @PostMapping("/povertyAssistance/add")
    public <T> BaseResponse<T> insertPovertyAssistance(@RequestBody @Validated({InsertGroup.class}) PovertyAssistance povertyAssistance) {
        return povertyAssistanceService.insertPovertyAssistance(povertyAssistance);
    }

    @PreAuthorize("hasAuthority('poverty_assistance:update')")
    @PutMapping("/povertyAssistance/update")
    public <T> BaseResponse<T> updatePovertyAssistance(@RequestBody @Validated({UpdateGroup.class}) PovertyAssistance povertyAssistance) {
        return povertyAssistanceService.updatePovertyAssistance(povertyAssistance);
    }

    @PreAuthorize("hasAuthority('poverty_assistance:delete')")
    @DeleteMapping("/povertyAssistance/delete/{povertyAssistanceId}")
    public <T> BaseResponse<T> deletePovertyAssistance(@PathVariable String povertyAssistanceId) {
        return povertyAssistanceService.deletePovertyAssistance(povertyAssistanceId);
    }

    @PreAuthorize("hasAuthority('student_poverty_assistance:insert')")
    @PostMapping("/student/povertyAssistance/add")
    public <T> BaseResponse<T> addStudentPovertyAssistance(@RequestBody @Validated({InsertGroup.class}) StudentPovertyAssistance studentPovertyAssistance) {
        return povertyAssistanceService.addStudentPovertyAssistance(studentPovertyAssistance);
    }

    @PreAuthorize("hasAuthority('student_poverty_assistance:insert')")
    @PostMapping("/student/povertyAssistance/adds")
    public <T> BaseResponse<T> importStudentPovertyAssistances(@RequestBody @Validated({InsertGroup.class}) ValidateList<StudentPovertyAssistance> studentPovertyAssistances) {
        return povertyAssistanceService.importStudentPovertyAssistance(studentPovertyAssistances);
    }

    @PreAuthorize("hasAuthority('student_poverty_assistance:select')")
    @PostMapping("/student/povertyAssistance/gets")
    public BaseResponse<Page<StudentPovertyAssistanceItem>> getStudentPovertyAssistance(@RequestBody PovertyAssistanceQuery query) {
        return povertyAssistanceService.getStudentPovertyAssistance(query);
    }

    @PreAuthorize("hasAuthority('student_poverty_assistance:update')")
    @PutMapping("/student/povertyAssistance/update")
    public <T> BaseResponse<T> updateStudentPovertyAssistance(@RequestBody @Validated({UpdateGroup.class}) StudentPovertyAssistance studentPovertyAssistance) {
        return povertyAssistanceService.updateStudentPovertyAssistance(studentPovertyAssistance);
    }

    @PreAuthorize("hasAuthority('student_poverty_assistance:delete')")
    @DeleteMapping("/student/povertyAssistance/delete/{studentPovertyAssistanceId}")
    public <T> BaseResponse<T> deleteStudentPovertyAssistance(@PathVariable String studentPovertyAssistanceId) {
        return povertyAssistanceService.deleteStudentPovertyAssistance(studentPovertyAssistanceId);
    }

    @PreAuthorize("hasAuthority('student_poverty_assistance:select')")
    @PostMapping("/student/povertyAssistance/stat")
    public BaseResponse<List<PovertyAssistanceStatGroup>> getStudentPovertyAssistanceStat(@RequestBody PovertyAssistanceStatQuery query) {
        return povertyAssistanceService.getStudentPovertyAssistanceStat(query);
    }
}
