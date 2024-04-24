package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.entity.dto.cadre.*;
import edu.guet.studentworkmanagementsystem.entity.po.cadre.Cadre;
import edu.guet.studentworkmanagementsystem.entity.po.cadre.StudentCadre;
import edu.guet.studentworkmanagementsystem.entity.vo.cadre.StudentCadreVO;
import edu.guet.studentworkmanagementsystem.service.cadre.CadreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CadreController {
    @Autowired
    private CadreService cadreService;
    @PreAuthorize("hasAuthority('cadre:insert')")
    @PostMapping("/cadre/add")
    public BaseResponse<Cadre> addCadre(@RequestBody @Validated({InsertGroup.class}) Cadre cadre) {
        return cadreService.insertCadre(cadre);
    }
    @PreAuthorize("hasAuthority('cadre:update')")
    @PutMapping("/cadre/update")
    public <T> BaseResponse<T> updateCadre(@RequestBody @Valid CadreDTO cadreDTO) {
        return cadreService.updateCadre(cadreDTO);
    }
    @PreAuthorize("hasAuthority('cadre:select')")
    @GetMapping("/cadre/gets")
    public BaseResponse<List<Cadre>> getAllCadre() {
        return cadreService.getAllCadres();
    }
    @PreAuthorize("hasAuthority('cadre:delete')")
    @DeleteMapping("/cadre/delete/{cadreId}")
    public <T> BaseResponse<T> deleteMajor(@PathVariable String cadreId) {
        return cadreService.deleteCadre(cadreId);
    }
    @PreAuthorize("hasAuthority('student_cadre:insert')")
    @PostMapping("/student_cadre/add")
    public BaseResponse<StudentCadre> arrangePosition(@RequestBody @Valid InsertStudentCadreDTO insertStudentCadreDTO){
        return cadreService.arrangePosition(insertStudentCadreDTO) ;
    }
    @PreAuthorize("hasAuthority('student_cadre:insert')")
    @PostMapping("/student_cadre/adds")
    public BaseResponse<StudentCadre> arrangePositions(@RequestBody @Valid InsertStudentCadreList insertStudentCadreList){
        return cadreService.arrangePositions(insertStudentCadreList) ;
    }
    @PreAuthorize("hasAuthority('student_cadre:update')")
    @PutMapping("/student_cadre/update")
    public BaseResponse<StudentCadre> updateStudentCadreInfo(@RequestBody @Valid UpdateStudentCadreDTO updateStudentCadreDTO){
        return cadreService.updateStudentCadre(updateStudentCadreDTO) ;
    }
     @PreAuthorize("hasAuthority('student_cadre:delete')")
    @DeleteMapping("/student_cadre/delete/{studentCadreId}")
    public BaseResponse<StudentCadre> deleteStudentCadre(@PathVariable String studentCadreId){
        return cadreService.deleteStudentCadre(studentCadreId) ;
    }
    @PreAuthorize(
            "hasAuthority('student:select') " +
            " and hasAuthority('student_cadre:select') " +
            " and hasAuthority('cadre:select') " +
            " and hasAuthority('major:select') "
    )
    @PostMapping("/student_cadre/gets")
    public BaseResponse<Page<StudentCadreVO>> getAllStudentAcademicWork(@RequestBody CadreQuery cadreQuery){
        return cadreService.getAllStudentAcademicWork(cadreQuery) ;
    }
}
