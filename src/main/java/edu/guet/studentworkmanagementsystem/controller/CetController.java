package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.cet.*;
import edu.guet.studentworkmanagementsystem.entity.vo.cet.StudentCetVO;
import edu.guet.studentworkmanagementsystem.service.cet.CetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/cet")
public class CetController {
    @Autowired
    private CetService cetService;
    @PreAuthorize("hasAuthority('student_cet:insert')")
    @PostMapping("/adds")
    public <T> BaseResponse<T> importCETScore(@RequestBody @Valid InsertCetDTOList insertCetDTOList) {
        return cetService.importCETScore(insertCetDTOList);
    }
    @PreAuthorize("hasAuthority('student_cet:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> insertStudentCet(@RequestBody @Valid InsertStudentCetDTO insertStudentCetDTO) {
        return cetService.insertStudentCet(insertStudentCetDTO);
    }
    @PreAuthorize("hasAuthority('student_cet:select') and hasAuthority('student:select') and hasAuthority('major:select')")
    @PostMapping("/gets")
    public BaseResponse<Page<StudentCetVO>> getAllRecord(@RequestBody CETQuery query) {
        return cetService.getAllRecord(query);
    }
    @PreAuthorize("hasAuthority('student_cet:select')")
    @GetMapping("/gets/optional_exam_date")
    public BaseResponse<List<String>> getOptionalExamDate() {
        return cetService.getOptionalExamDate();
    }
    @PreAuthorize("hasAuthority('student_cet:update')")
    @PutMapping("/update")
    public  <T> BaseResponse<T> updateStudentCET(@RequestBody @Valid UpdateStudentCetDTO updateStudentCetDTO) {
        return cetService.updateStudentCET(updateStudentCetDTO);
    }
    @PreAuthorize("hasAuthority('student_cet:delete')")
    @DeleteMapping("/delete/{studentCetId}")
    public <T> BaseResponse<T> deleteStudentCET(@PathVariable String studentCetId) {
        return cetService.deleteStudentCET(studentCetId);
    }
    @PreAuthorize("hasAuthority('student_cet:select')")
    @PostMapping("/stat")
    public BaseResponse<HashMap<String, Object>> stat(@RequestBody CetStatQuery query) {
        return cetService.getCetStatistics(query);
    }
}
