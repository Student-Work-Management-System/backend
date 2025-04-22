package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.entity.dto.precaution.PrecautionQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.precaution.PrecautionStatQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.schoolPrecaution.PrecautionList;
import edu.guet.studentworkmanagementsystem.entity.dto.schoolPrecaution.StudentSchoolPrecautionRequest;
import edu.guet.studentworkmanagementsystem.entity.po.precaution.StudentPrecaution;
import edu.guet.studentworkmanagementsystem.entity.vo.schoolPrecaution.StudentSchoolPrecautionItem;
import edu.guet.studentworkmanagementsystem.service.schoolPrecaution.PrecautionService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
@RequestMapping("/school_precaution")
public class PrecautionController {
    @Autowired
    private PrecautionService precautionService;
    @PreAuthorize("hasAuthority('student_school_precaution:insert')")
    @PostMapping("/adds")
    public <T> BaseResponse<T> importSchoolPrecaution(@RequestBody @Validated({InsertGroup.class}) PrecautionList schoolPrecautions) {
        return precautionService.importSchoolPrecaution(schoolPrecautions);
    }
    @PreAuthorize("hasAuthority('student_school_precaution:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> insertSchoolPrecaution(@RequestBody @Validated({InsertGroup.class}) StudentPrecaution schoolPrecaution) {
        return precautionService.insertSchoolPrecaution(schoolPrecaution);
    }
    @PreAuthorize("hasAuthority('student_school_precaution:update')")
    @PutMapping("/update")
    public <T> BaseResponse<T> updateSchoolPrecaution(@RequestBody @Valid StudentSchoolPrecautionRequest schoolPrecautionDTO) {
        return precautionService.updateSchoolPrecaution(schoolPrecautionDTO);
    }
    @PreAuthorize("hasAuthority('student_school_precaution:delete')")
    @DeleteMapping("/delete/{studentSchoolPrecautionId}")
    public <T> BaseResponse<T> deleteSchoolPrecaution(@PathVariable String studentSchoolPrecautionId) {
        return precautionService.deleteSchoolPrecaution(studentSchoolPrecautionId);
    }
    @PreAuthorize("hasAuthority('student_school_precaution:select') and hasAuthority('student:select') and hasAuthority('major:select')")
    @PostMapping("/gets")
    public BaseResponse<Page<StudentSchoolPrecautionItem>> getAllRecords(@RequestBody PrecautionQuery query) {
        return precautionService.getAllRecords(query);
    }
    @PreAuthorize("hasAuthority('student_school_precaution:select')")
    @PostMapping("/stat")
    public BaseResponse<HashMap<String, Object>> stat(@RequestBody PrecautionStatQuery query) {
        return precautionService.stat(query);
    }
    @PreAuthorize("hasAuthority('student_school_precaution:select')")
    @PostMapping("/download")
    public void download(@RequestBody PrecautionStatQuery query, HttpServletResponse response) {
        precautionService.download(query, response);
    }
}
