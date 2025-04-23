package edu.guet.studentworkmanagementsystem.service.leave;

import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeaveEvidence;

import java.util.List;

public interface StudentLeaveEvidenceService extends IService<StudentLeaveEvidence> {
    void addEvidence(List<StudentLeaveEvidence> evidences);
}
