package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.*;
import edu.guet.studentworkmanagementsystem.entity.po.competition.Competition;
import edu.guet.studentworkmanagementsystem.entity.po.competition.StudentCompetitionAudit;
import edu.guet.studentworkmanagementsystem.entity.vo.competition.StudentCompetitionItem;
import edu.guet.studentworkmanagementsystem.service.competition.CompetitionAuditService;
import edu.guet.studentworkmanagementsystem.service.competition.CompetitionService;
import edu.guet.studentworkmanagementsystem.service.competition.StudentCompetitionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompetitionController {
    @Autowired
    private CompetitionService competitionService;
    @Autowired
    private StudentCompetitionService studentCompetitionService;
    @Autowired
    private CompetitionAuditService competitionAuditService;

    @PreAuthorize("hasAuthority('competition:select')")
    @GetMapping("/competition/gets")
    public BaseResponse<Page<Competition>> getAllCompetitions(@RequestBody CompetitionQuery query) {
        return competitionService.getAllCompetitions(query);
    }
    @PreAuthorize("hasAuthority('competition:insert')")
    @PostMapping("/competition/adds")
    public <T> BaseResponse<T> importCompetition(@RequestBody @Validated({InsertGroup.class}) ValidateList<Competition> competitions) {
        return competitionService.importCompetition(competitions);
    }
    @PreAuthorize("hasAuthority('competition:insert')")
    @PostMapping("/competition/add")
    public <T> BaseResponse<T> insertCompetition(@RequestBody @Validated({InsertGroup.class}) Competition competition) {
        return competitionService.insertCompetition(competition);
    }
    @PreAuthorize("hasAuthority('competition:update')")
    @PutMapping("/competition/update")
    public <T> BaseResponse<T> updateCompetition(@RequestBody @Validated({UpdateGroup.class}) Competition competition) {
        return competitionService.updateCompetition(competition);
    }
    @PreAuthorize("hasAuthority('competition:delete')")
    @DeleteMapping("/competition/delete/{competitionId}")
    public <T> BaseResponse<T> updateCompetition(@PathVariable String competitionId) {
        return competitionService.deleteCompetition(competitionId);
    }
    @PreAuthorize("hasAuthority('student_competition:select:own')")
    @GetMapping("/student_competition/getOwn")
    public BaseResponse<List<StudentCompetitionItem>> getOwnStudentCompetition() {
        return studentCompetitionService.getOwnStudentCompetition();
    }
    @PreAuthorize("hasAuthority('student_competition:insert')")
    @PostMapping("/student_competition/add")
    public <T> BaseResponse<T> insertStudentCompetition(@RequestBody @Valid StudentCompetitionWithMember studentCompetitionWithMember) {
        return studentCompetitionService.insertStudentCompetition(studentCompetitionWithMember);
    }
    @PreAuthorize("hasAuthority('student_competition:delete')")
    @DeleteMapping("/student_competition/delete/{studentCompetitionId}")
    public <T> BaseResponse<T> deleteStudentCompetition(@PathVariable String studentCompetitionId) {
        return studentCompetitionService.deleteStudentCompetition(studentCompetitionId);
    }
    @PreAuthorize("hasAuthority('student_competition:select')")
    @PostMapping("/student_competition/gets")
    public BaseResponse<Page<StudentCompetitionItem>> getStudentCompetitions(@RequestBody StudentCompetitionQuery query) {
        return studentCompetitionService.getStudentCompetitions(query);
    }
    @PreAuthorize("hasAuthority('student_competition:update')")
    @PutMapping("/student_competition/update")
    public <T> BaseResponse<T> updateStudentCompetitionAudit(@RequestBody @Validated({UpdateGroup.class}) StudentCompetitionAudit studentCompetitionAudit) {
        return competitionAuditService.updateCompetitionAudit(studentCompetitionAudit);
    }
}
