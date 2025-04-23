package edu.guet.studentworkmanagementsystem.service.leave.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeaveEvidence;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.leave.StudentLeaveEvidenceMapper;
import edu.guet.studentworkmanagementsystem.service.leave.StudentLeaveEvidenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
}
