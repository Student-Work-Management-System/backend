package edu.guet.studentworkmanagementsystem.controller;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.po.other.Degree;
import edu.guet.studentworkmanagementsystem.entity.po.other.Grade;
import edu.guet.studentworkmanagementsystem.entity.po.other.Politic;
import edu.guet.studentworkmanagementsystem.service.other.OtherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/other")
@RestController
public class OtherController {
    @Autowired
    private OtherService otherService;
    @PreAuthorize("hasAuthority('grade:select')")
    @GetMapping("/allGrade")
    public BaseResponse<List<Grade>> getAllGrades() {
        return otherService.getAllGrades();
    }
    @PreAuthorize("hasAuthority('degree:select')")
    @GetMapping("/allDegree")
    public BaseResponse<List<Degree>> getAllDegree() {
        return otherService.getAllDegrees();
    }
    @PreAuthorize("hasAuthority('politic:select')")
    @GetMapping("/allPolitic")
    public BaseResponse<List<Politic>> getAllPolitics() {
        return otherService.getAllPolitics();
    }
}
