package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import edu.guet.studentworkmanagementsystem.entity.dto.other.CounselorQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.other.CounselorRequest;
import edu.guet.studentworkmanagementsystem.entity.po.other.*;
import edu.guet.studentworkmanagementsystem.entity.vo.other.CounselorItem;
import edu.guet.studentworkmanagementsystem.entity.vo.other.UserWithCounselorRole;
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

    @PreAuthorize("hasAuthority('grade:insert')")
    @PostMapping("/addGrade")
    public <T> BaseResponse <T> addGrade(@RequestBody @Validated({InsertGroup.class}) Grade grade) {
        return otherService.addGrade(grade);
    }

    @PreAuthorize("hasAuthority('degree:select')")
    @GetMapping("/allDegree")
    public BaseResponse<List<Degree>> getAllDegree() {
        return otherService.getAllDegrees();
    }

    @PreAuthorize("hasAuthority('degree:insert')")
    @PostMapping("/addDegree")
    public <T> BaseResponse <T> addDegree(@RequestBody @Validated({InsertGroup.class}) Degree degree) {
        return otherService.addDegree(degree);
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

    @PreAuthorize("hasAuthority('counselor:select')")
    @PostMapping("/counselor/gets")
    public BaseResponse<Page<CounselorItem>> getCounselors(@RequestBody CounselorQuery query) {
        return otherService.getAllCounselors(query);
    }

    @PreAuthorize("hasAuthority('counselor:delete')")
    @DeleteMapping("/counselor/delete/{uid}")
    public <T> BaseResponse<T> deleteCounselor(@PathVariable String uid) {
        return otherService.deleteCounselor(uid);
    }

    @PreAuthorize("hasAuthority('counselor:update')")
    @PutMapping("/counselor/update")
    public <T> BaseResponse<T> updateCounselor(@RequestBody CounselorRequest counselor) {
        return otherService.updateCounselor(counselor);
    }

    @PreAuthorize("hasAuthority('counselor:select')")
    @GetMapping("/counselor/gets/{gradeId}")
    public BaseResponse<List<UserWithCounselorRole>> getCounselor(@PathVariable String gradeId) {
        return otherService.getOptionalCounselors(gradeId);
    }

    @PreAuthorize("hasAuthority('counselor:select')")
    @GetMapping("/counselor/optional")
    public BaseResponse<List<UserWithCounselorRole>> getCounselorsOptional() {
        return otherService.getOptionalCounselors();
    }

    @PreAuthorize("hasAuthority('counselor:insert')")
    @PostMapping("/counselor/add")
    public <T> BaseResponse<T> addCounselor(@RequestBody CounselorRequest counselor) {
        return otherService.addCounselors(counselor);
    }

}
