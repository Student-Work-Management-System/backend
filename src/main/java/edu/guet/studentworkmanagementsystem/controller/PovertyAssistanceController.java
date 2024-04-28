package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance.*;
import edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.PovertyAssistance;
import edu.guet.studentworkmanagementsystem.service.povertyAssistance.PovertyAssistanceService;
import jakarta.validation.Valid;
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
    @GetMapping("/poverty_assistance/gets")
    public BaseResponse<List<PovertyAssistance>> getAllPovertyAssistance() {
        return povertyAssistanceService.getAllPovertyAssistance();
    }
    @PreAuthorize("hasAuthority('poverty_assistance:insert')")
    @PostMapping("/poverty_assistance/adds")
    public <T> BaseResponse<T> importPovertyAssistance(@RequestBody @Validated({InsertGroup.class}) PovertyAssistanceList povertyAssistanceList) {
        return povertyAssistanceService.importPovertyAssistance(povertyAssistanceList);
    }
    @PreAuthorize("hasAuthority('poverty_assistance:insert')")
    @PostMapping("/poverty_assistance/add")
    public <T> BaseResponse<T> insertPovertyAssistance(@RequestBody @Validated({InsertGroup.class}) PovertyAssistance povertyAssistance) {
        return povertyAssistanceService.insertPovertyAssistance(povertyAssistance);
    }
    @PreAuthorize("hasAuthority('poverty_assistance:update')")
    @PutMapping("/poverty_assistance/update")
    public <T> BaseResponse<T> updatePovertyAssistance(@RequestBody @Validated({UpdateGroup.class}) PovertyAssistance povertyAssistance) {
        return povertyAssistanceService.updatePovertyAssistance(povertyAssistance);
    }
    @PreAuthorize("hasAuthority('poverty_assistance:delete')")
    @DeleteMapping("/poverty_assistance/delete/{povertyAssistanceId}")
    public <T> BaseResponse<T> deletePovertyAssistance(@PathVariable String povertyAssistanceId) {
        return povertyAssistanceService.deletePovertyAssistance(povertyAssistanceId);
    }
    @PreAuthorize("hasAuthority('student_poverty_assistance:insert')")
    @PostMapping("/student_poverty_assistance/add")
    public <T> BaseResponse<T> addStudentPovertyAssistance(@RequestBody @Valid InsertStudentPovertyAssistanceDTO insertStudentPovertyAssistanceDTO) {
        return povertyAssistanceService.addStudentPovertyAssistance(insertStudentPovertyAssistanceDTO);
    }
    @PreAuthorize("hasAuthority('student_poverty_assistance:insert')")
    @PostMapping("/student_poverty_assistance/adds")
    public <T> BaseResponse<T> importStudentPovertyAssistances(@RequestBody @Valid InsertStudentPovertyAssistanceList list) {
        return povertyAssistanceService.importStudentPovertyAssistance(list);
    }
    @PreAuthorize(
            "hasAuthority('student_poverty_assistance:select') " +
            "and hasAuthority('poverty_assistance:select') " +
            "and hasAuthority('student:select') " +
            "and hasAuthority('major:select') "
    )
    @PostMapping("/student_poverty_assistance/gets")
    public BaseResponse<Page<StudentStudentPovertyAssistanceVO>> getStudentPovertyAssistance(@RequestBody PovertyAssistanceQuery query) {
        return povertyAssistanceService.getStudentPovertyAssistance(query);
    }
    @PreAuthorize("hasAuthority('student_poverty_assistance:update')")
    @PutMapping("/student_poverty_assistance/update")
    public <T> BaseResponse<T> updateStudentPovertyAssistance(@RequestBody @Valid UpdateStudentPovertyAssistanceDTO updateStudentPovertyAssistanceDTO) {
        return povertyAssistanceService.updateStudentPovertyAssistance(updateStudentPovertyAssistanceDTO);
    }
    @PreAuthorize("hasAuthority('student_poverty_assistance:delete')")
    @DeleteMapping("/student_poverty_assistance/delete/{studentPovertyAssistanceId}")
    public <T> BaseResponse<T> deleteStudentPovertyAssistance(@PathVariable String studentPovertyAssistanceId) {
        return povertyAssistanceService.deleteStudentPovertyAssistance(studentPovertyAssistanceId);
    }
}
