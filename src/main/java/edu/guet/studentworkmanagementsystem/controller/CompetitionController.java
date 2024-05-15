package edu.guet.studentworkmanagementsystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.CompetitionAuditDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.CompetitionList;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.CompetitionQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.StudentCompetitionDTO;
import edu.guet.studentworkmanagementsystem.entity.po.competition.Competition;
import edu.guet.studentworkmanagementsystem.entity.vo.competition.StudentCompetitionPassedRecord;
import edu.guet.studentworkmanagementsystem.entity.vo.competition.StudentCompetitionVO;
import edu.guet.studentworkmanagementsystem.service.competition.CompetitionService;
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
    @PreAuthorize("hasAuthority('competition:select')")
    @GetMapping("/competition/gets")
    public BaseResponse<Page<Competition>> getAllCompetitions(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "50") int pageSize) {
        return competitionService.getAllCompetitions(pageNo, pageSize);
    }
    @PreAuthorize("hasAuthority('competition:insert')")
    @PostMapping("/competition/adds")
    public <T> BaseResponse<T> importCompetition(@RequestBody @Validated({InsertGroup.class}) CompetitionList competitionList) {
        return competitionService.importCompetition(competitionList);
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
    @PreAuthorize("hasAuthority('student_competition:select')")
    @GetMapping("/student_competition/get/{studentId}")
    public BaseResponse<List<StudentCompetitionVO>> getOwnStudentCompetition(@PathVariable String studentId) {
        return competitionService.getOwnStudentCompetition(studentId);
    }
    @PreAuthorize("hasAuthority('student_competition:insert')")
    @PostMapping("/student_competition/add")
    public <T> BaseResponse<T> insertStudentCompetition(@RequestBody @Valid StudentCompetitionDTO studentCompetitionDTO) throws JsonProcessingException {
        return competitionService.insertStudentCompetition(studentCompetitionDTO);
    }
    @PreAuthorize("hasAuthority('student_competition:update') and hasAuthority('student_competition_claim:insert')")
    @PutMapping("/student_competition/audit")
    public  <T> BaseResponse<T> auditStudentCompetition(@RequestBody @Valid CompetitionAuditDTO competitionAuditDTO) throws JsonProcessingException {
        return competitionService.auditStudentCompetition(competitionAuditDTO);
    }
    @PreAuthorize("hasAuthority('student_competition:delete') and hasAuthority('student_competition_claim:delete')")
    @DeleteMapping("/student_competition/delete/{studentCompetitionId}")
    public <T> BaseResponse<T> deleteStudentCompetition(@PathVariable String studentCompetitionId) {
        return competitionService.deleteStudentCompetition(studentCompetitionId);
    }
    @PreAuthorize("hasAuthority('student_competition:select') and hasAuthority('student:select')")
    @PostMapping("/student_competition/gets")
    public BaseResponse<Page<StudentCompetitionVO>> getAllStudentCompetition(@RequestBody CompetitionQuery query) {
        return competitionService.getAllStudentCompetition(query);
    }
    @PreAuthorize( "hasAuthority('student_competition:select')")
    @GetMapping("/student_competition/gets/pass")
    public BaseResponse<Page<StudentCompetitionPassedRecord>> getAllPassStudentCompetition(@RequestBody CompetitionQuery query) {
        return competitionService.getAllPassedStudentCompetition(query);
    }
}
