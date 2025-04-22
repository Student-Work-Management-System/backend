package edu.guet.studentworkmanagementsystem.service.leave.impl;

import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeave;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.leave.StudentLeaveMapper;
import edu.guet.studentworkmanagementsystem.service.leave.StudentLeaveService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static edu.guet.studentworkmanagementsystem.entity.po.leave.table.StudentLeaveTableDef.STUDENT_LEAVE;

@Service
public class StudentLeaveServiceImpl extends ServiceImpl<StudentLeaveMapper, StudentLeave> implements StudentLeaveService {

    @Override
    @Transactional
    public void addStudentLeave(StudentLeave studentLeave) {
        int i = mapper.insert(studentLeave);
        if (i == 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> destroyLeave(String leaveId) {
        boolean update = UpdateChain.of(StudentLeave.class)
                .set(STUDENT_LEAVE.DESTROYED, true)
                .where(STUDENT_LEAVE.LEAVE_ID.eq(leaveId))
                .update();
        if (!update)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }
}
