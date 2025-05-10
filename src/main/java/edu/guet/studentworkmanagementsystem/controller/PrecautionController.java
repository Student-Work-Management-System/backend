package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.precaution.PrecautionQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.precaution.PrecautionStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.precaution.StudentPrecaution;
import edu.guet.studentworkmanagementsystem.entity.vo.precaution.StudentPrecautionItem;
import edu.guet.studentworkmanagementsystem.entity.vo.precaution.StudentPrecautionStatGroup;
import edu.guet.studentworkmanagementsystem.service.precaution.PrecautionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/precaution")
public class PrecautionController {
    @Autowired
    private PrecautionService precautionService;

    @PreAuthorize("hasAuthority('student_precaution:insert')")
    @PostMapping("/adds")
    public <T> BaseResponse<T> importPrecaution(@RequestBody @Validated({InsertGroup.class}) ValidateList<StudentPrecaution> precautions) {
        return precautionService.importPrecaution(precautions);
    }

    @PreAuthorize("hasAuthority('student_precaution:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> insertPrecaution(@RequestBody @Validated({InsertGroup.class}) StudentPrecaution schoolPrecaution) {
        return precautionService.insertPrecaution(schoolPrecaution);
    }

    @PreAuthorize("hasAuthority('student_precaution:update')")
    @PutMapping("/update")
    public <T> BaseResponse<T> updatePrecaution(@RequestBody @Validated StudentPrecaution studentPrecaution) {
        return precautionService.updatePrecaution(studentPrecaution);
    }

    @PreAuthorize("hasAuthority('student_precaution:delete')")
    @DeleteMapping("/delete/{studentPrecautionId}")
    public <T> BaseResponse<T> deleteSchoolPrecaution(@PathVariable String studentPrecautionId) {
        return precautionService.deletePrecaution(studentPrecautionId);
    }

    @PreAuthorize("hasAuthority('student_precaution:select')")
    @PostMapping("/gets")
    public BaseResponse<Page<StudentPrecautionItem>> getAllRecords(@RequestBody PrecautionQuery query) {
        return precautionService.getAllRecords(query);
    }

    @PreAuthorize("hasAuthority('student_precaution:select')")
    @PostMapping("/stat")
    public BaseResponse<List<StudentPrecautionStatGroup>> stat(@RequestBody PrecautionStatQuery query) {
        return precautionService.getStat(query);
    }
}
