package edu.guet.studentworkmanagementsystem.controller;


import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.cadre.CadreDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.cadre.CadreQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.cadre.StudentCadreDTO;
import edu.guet.studentworkmanagementsystem.entity.po.cadre.Cadre;
import edu.guet.studentworkmanagementsystem.entity.po.cadre.StudentCadre;
import edu.guet.studentworkmanagementsystem.entity.vo.cadre.StudentCadreVO;
import edu.guet.studentworkmanagementsystem.service.cadre.CadreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cadre")
public class CadreController {

    @Autowired
    CadreService cadreService ;

    @PreAuthorize("hasAuthority('cadre:insert')")
    @PostMapping("/adds")
    public BaseResponse<List<Cadre>> addCadres(@RequestBody List<Cadre> cadres) {
        return cadreService.importCadres(cadres);
    }

    @PreAuthorize("hasAuthority('cadre:insert')")
    @PostMapping("/add")
    public BaseResponse<Cadre> addCadre(@RequestBody Cadre cadre) {
        return cadreService.insertCadre(cadre);
    }

    @PreAuthorize("hasAuthority('cadre:update')")
    @PutMapping("/update")
    public <T> BaseResponse<T> updateCadre(@RequestBody CadreDTO cadreDTO) {
        return cadreService.updateCadre(cadreDTO);
    }

    @PreAuthorize("hasAuthority('cadre:select')")
    @GetMapping("/gets")
    public BaseResponse<List<Cadre>> getAllCadre() {
        return cadreService.getAllCadres();
    }

    @PreAuthorize("hasAuthority('cadre:delete')")
    @DeleteMapping("/delete/{cadreId}")
    public <T> BaseResponse<T> deleteMajor(@PathVariable String cadreId) {
        return cadreService.deleteCadre(cadreId);
    }

     @PreAuthorize("hasAuthority('student_cadre:insert')")
    @PostMapping("/add/sc")
    public BaseResponse<StudentCadre> arrangePositions(@RequestBody StudentCadre studentCadre){
        return cadreService.arrangePositions(studentCadre) ;
    }

     @PreAuthorize("hasAuthority('student_cadre:update')")
    @PutMapping("/update/scinfo")
    public BaseResponse<StudentCadre> updateStudentCadreInfo(@RequestBody StudentCadreDTO studentCadreDTO){
        return cadreService.updateStudentCadreInfo(studentCadreDTO) ;
    }

     @PreAuthorize("hasAuthority('student_cadre:update')")
    @PutMapping("/update/sc")
    public BaseResponse<StudentCadre> updateStudentCadre(@RequestBody Map<String,String> data ){
        return cadreService.updateStudentCadre(data.get("studentCadreId"), data.get("cadreId")) ;
    }

     @PreAuthorize("hasAuthority('student_cadre:delete')")
    @DeleteMapping("/delete/sc/{studentCadreId}")
    public BaseResponse<StudentCadre> deleteStudentCadre(@PathVariable  String studentCadreId){
        return cadreService.deleteStudentCadre(studentCadreId) ;
    }

    @PreAuthorize(
            "hasAuthority('student:select') " +
                    "and hasAuthority('student_cadre:select') " +
                    "and hasAuthority('cadre:select') " +
                    "and hasAuthority('major:select') "
    )
    @PostMapping("/gets/sc")
    public BaseResponse<Page<StudentCadreVO>> getAllStudentAcademicWork(@RequestBody CadreQuery cadreQuery){
        return cadreService.getAllStudentAcademicWork(cadreQuery) ;
    }



}
