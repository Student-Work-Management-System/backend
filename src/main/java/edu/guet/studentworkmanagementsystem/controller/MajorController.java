package edu.guet.studentworkmanagementsystem.controller;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import edu.guet.studentworkmanagementsystem.entity.po.major.Major;
import edu.guet.studentworkmanagementsystem.service.major.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/major")
public class MajorController {
    @Autowired
    private MajorService majorService;
    @PreAuthorize("hasAuthority('major:select')")
    @GetMapping("/gets")
    public BaseResponse<List<Major>> getAllMajor() {
        return majorService.getMajors();
    }
    @PreAuthorize("hasAuthority('major:update')")
    @PutMapping("/update")
    public <T> BaseResponse<T> updateMajor(@RequestBody @Validated({UpdateGroup.class}) Major major) {
        return majorService.updateMajor(major);
    }
    @PreAuthorize("hasAuthority('major:delete')")
    @DeleteMapping("/delete/{majorId}")
    public <T> BaseResponse<T> deleteMajor(@PathVariable String majorId) {
        return majorService.deleteMajor(majorId);
    }
    @PreAuthorize("hasAuthority('major:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> addMajor(@RequestBody @Validated({InsertGroup.class}) Major major) {
        return majorService.addMajor(major);
    }
}
