package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.precaution.PrecautionQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.schoolPrecaution.StudentSchoolPrecautionDTO;
import edu.guet.studentworkmanagementsystem.entity.po.schoolPrecaution.StudentSchoolPrecaution;
import edu.guet.studentworkmanagementsystem.entity.vo.schoolPrecaution.StudentSchoolPrecautionVO;
import edu.guet.studentworkmanagementsystem.service.schoolPrecaution.PrecautionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/school_precaution")
public class PrecautionController {
    @Autowired
    private PrecautionService precautionService;
    @PreAuthorize("hasAuthority('student_school_precaution:insert')")
    @PostMapping("/adds")
    public <T> BaseResponse<T> importSchoolPrecaution(@RequestBody List<StudentSchoolPrecaution> schoolPrecautions) {
        return precautionService.importSchoolPrecaution(schoolPrecautions);
    }
    @PreAuthorize("hasAuthority('student_school_precaution:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> insertSchoolPrecaution(@RequestBody StudentSchoolPrecaution schoolPrecaution) {
        return precautionService.insertSchoolPrecaution(schoolPrecaution);
    }
    @PreAuthorize("hasAuthority('student_school_precaution:update')")
    @PutMapping("/update")
    public <T> BaseResponse<T> updateSchoolPrecaution(@RequestBody @Valid StudentSchoolPrecautionDTO schoolPrecautionDTO) {
        return precautionService.updateSchoolPrecaution(schoolPrecautionDTO);
    }
    @PreAuthorize("hasAuthority('student_school_precaution:delete')")
    @DeleteMapping("/delete/{studentSchoolPrecautionId}")
    public <T> BaseResponse<T> deleteSchoolPrecaution(@PathVariable String studentSchoolPrecautionId) {
        return precautionService.deleteSchoolPrecaution(studentSchoolPrecautionId);
    }
    @PreAuthorize("hasAuthority('student_school_precaution:select') and hasAuthority('student:select') and hasAuthority('major:select')")
    @PostMapping("/gets")
    public BaseResponse<Page<StudentSchoolPrecautionVO>> getAllRecords(@RequestBody PrecautionQuery query) {
        return precautionService.getAllRecords(query);
    }
}
