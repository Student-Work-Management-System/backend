package edu.guet.studentworkmanagementsystem.controller;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import edu.guet.studentworkmanagementsystem.entity.po.other.Degree;
import edu.guet.studentworkmanagementsystem.entity.po.other.Grade;
import edu.guet.studentworkmanagementsystem.entity.po.other.Major;
import edu.guet.studentworkmanagementsystem.entity.po.other.Politic;
import edu.guet.studentworkmanagementsystem.service.other.OtherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasAuthority('major:select')")
    @GetMapping("/getAllMajors")
    public BaseResponse<List<Major>> getAllMajor() {
        return otherService.getMajors();
    }
    @PreAuthorize("hasAuthority('major:update')")
    @PutMapping("/updateMajor")
    public <T> BaseResponse<T> updateMajor(@RequestBody @Validated({UpdateGroup.class}) Major major) {
        return otherService.updateMajor(major);
    }
    @PreAuthorize("hasAuthority('major:delete')")
    @DeleteMapping("/deleteMajor/{majorId}")
    public <T> BaseResponse<T> deleteMajor(@PathVariable String majorId) {
        return otherService.deleteMajor(majorId);
    }
    @PreAuthorize("hasAuthority('major:insert')")
    @PostMapping("/addMajor")
    public <T> BaseResponse<T> addMajor(@RequestBody @Validated({InsertGroup.class}) Major major) {
        return otherService.addMajor(major);
    }
}
