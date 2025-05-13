package edu.guet.studentworkmanagementsystem.service.leave.impl;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.Common;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.*;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeave;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeaveAudit;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeaveEvidence;
import edu.guet.studentworkmanagementsystem.entity.vo.leave.StudentLeaveItem;
import edu.guet.studentworkmanagementsystem.entity.vo.leave.StudentLeaveStatGroup;
import edu.guet.studentworkmanagementsystem.service.leave.StudentLeaveAuditService;
import edu.guet.studentworkmanagementsystem.service.leave.LeaveService;
import edu.guet.studentworkmanagementsystem.service.leave.StudentLeaveEvidenceService;
import edu.guet.studentworkmanagementsystem.service.leave.StudentLeaveService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class LeaveServiceImpl implements LeaveService {
    @Autowired
    private StudentLeaveService leaveService;
    @Autowired
    private StudentLeaveAuditService auditService;
    @Autowired
    private StudentLeaveEvidenceService evidenceService;

    @Override
    @Transactional
    public <T> BaseResponse<T> addStudentLeave(StudentLeaveRequest request) {
        StudentLeave studentLeave = createStudentLeave(request);
        String leaveId = leaveService.addStudentLeave(studentLeave);
        List<StudentLeaveEvidence> evidences = createStudentLeaveEvidences(request.getEvidences(), leaveId);
        if (!evidences.isEmpty()) evidenceService.addEvidence(evidences);
        StudentLeaveAudit studentLeaveAudit = createStudentLeaveAudit(request, leaveId);
        auditService.addAudit(studentLeaveAudit);
        return ResponseUtil.success();
    }

    public StudentLeave createStudentLeave(StudentLeaveRequest request) {
        return StudentLeave.builder()
                .studentId(request.getStudentId())
                .type(request.getType())
                .reason(request.getReason())
                .target(request.getTarget())
                .targetDetail(request.getTargetDetail())
                .internship(request.isInternship())
                .startDay(request.getStartDay())
                .endDay(request.getEndDay())
                .build();
    }
    public StudentLeaveAudit createStudentLeaveAudit(StudentLeaveRequest request, String leaveId) {
        return StudentLeaveAudit.builder()
                .leaveId(leaveId)
                .counselorId(request.getCounselorId())
                .counselorHandleState(Common.WAITING.getValue())
                .counselorHandleTime(LocalDate.now())
                .leaderId(null)
                .leaderHandleState(Common.WAITING.getValue())
                .leaderHandleTime(LocalDate.now())
                .build();
    }
    public List<StudentLeaveEvidence> createStudentLeaveEvidences(List<String> evidences, String leaveId) {
        ArrayList<StudentLeaveEvidence> studentLeaveEvidences = new ArrayList<>();
        for (String path : evidences) {
            StudentLeaveEvidence build = StudentLeaveEvidence.builder()
                    .leaveId(leaveId)
                    .path(path)
                    .build();
            studentLeaveEvidences.add(build);
        }
        return studentLeaveEvidences;
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> studentDestroyLeave(String leaveId) {
        return leaveService.destroyLeave(leaveId);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> counselorAudit(AuditOperator operator) {
        return auditService.counselorAudit(operator);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> leaderAudit(AuditOperator operator) {
        return auditService.leaderAudit(operator);
    }

    @Override
    public BaseResponse<Page<StudentLeaveItem>> getStudentOnwRecord(StudentLeaveQuery query) {
        return leaveService.getOwnLeaves(query);
    }

    @Override
    public BaseResponse<Page<StudentLeaveItem>> getAuditRecord(AuditLeaveQuery query) {
        return leaveService.getLeaves(query);
    }

    @Override
    public BaseResponse<List<StudentLeaveStatGroup>> getStat(LeaveStatQuery query) {
        return leaveService.getStat(query);
    }
}
