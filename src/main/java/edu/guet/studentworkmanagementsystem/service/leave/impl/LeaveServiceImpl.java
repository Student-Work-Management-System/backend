package edu.guet.studentworkmanagementsystem.service.leave.impl;

import edu.guet.studentworkmanagementsystem.service.leave.StudentLeaveAuditService;
import edu.guet.studentworkmanagementsystem.service.leave.LeaveService;
import edu.guet.studentworkmanagementsystem.service.leave.StudentLeaveEvidenceService;
import edu.guet.studentworkmanagementsystem.service.leave.StudentLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LeaveServiceImpl implements LeaveService {
    @Autowired
    private StudentLeaveService leaveService;
    @Autowired
    private StudentLeaveAuditService auditService;
    @Autowired
    private StudentLeaveEvidenceService evidenceService;

}
