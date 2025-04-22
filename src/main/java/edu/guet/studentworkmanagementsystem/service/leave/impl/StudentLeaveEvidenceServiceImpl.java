package edu.guet.studentworkmanagementsystem.service.leave.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeaveEvidence;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.leave.StudentLeaveEvidenceMapper;
import edu.guet.studentworkmanagementsystem.service.leave.StudentLeaveEvidenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static edu.guet.studentworkmanagementsystem.entity.po.leave.table.StudentLeaveEvidenceTableDef.STUDENT_LEAVE_EVIDENCE;

@Service
public class StudentLeaveEvidenceServiceImpl extends ServiceImpl<StudentLeaveEvidenceMapper, StudentLeaveEvidence> implements StudentLeaveEvidenceService {

    @Override
    @Transactional
    public void addEvidence(List<StudentLeaveEvidence> evidences) {
        int i = mapper.insertBatch(evidences);
        int size = evidences.size();
        if (i != size)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public void deleteAllLeaveEvidence(String leaveId) {
        QueryWrapper deleteAllEvidenceWrapper = QueryWrapper.create().where(STUDENT_LEAVE_EVIDENCE.LEAVE_ID.eq(leaveId));
        int i = mapper.deleteByQuery(deleteAllEvidenceWrapper);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public void deleteLeaveEvidence(String evidenceId) {
        int i = mapper.deleteById(evidenceId);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
}
