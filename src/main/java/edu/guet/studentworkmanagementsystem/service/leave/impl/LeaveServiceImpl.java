package edu.guet.studentworkmanagementsystem.service.leave.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.LeaveList;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.LeaveQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.StudentLeaveAuditDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.StudentLeaveDTO;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeave;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeaveAudit;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import edu.guet.studentworkmanagementsystem.entity.vo.leave.StudentLeaveItem;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.leave.StudentLeaveAuditMapper;
import edu.guet.studentworkmanagementsystem.mapper.leave.StudentLeaveMapper;
import edu.guet.studentworkmanagementsystem.service.leave.LeaveService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static edu.guet.studentworkmanagementsystem.entity.po.leave.table.StudentLeaveAuditTableDef.STUDENT_LEAVE_AUDIT;
import static edu.guet.studentworkmanagementsystem.entity.po.leave.table.StudentLeaveTableDef.STUDENT_LEAVE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentTableDef.STUDENT;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.UserTableDef.USER;

@Service
public class LeaveServiceImpl extends ServiceImpl<StudentLeaveMapper, StudentLeave> implements LeaveService {
    @Autowired
    private StudentLeaveAuditMapper studentLeaveAuditMapper;
    @Override
    @Transactional
    public <T> BaseResponse<T> importStudentLeave(LeaveList studentLeaveList) {
        List<StudentLeave> studentLeaves = studentLeaveList.getStudentLeaves();
        int i = mapper.insertBatch(studentLeaves);
        if (i == studentLeaves.size())
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> insertStudentLeave(StudentLeave studentLeave) {
        int i = mapper.insert(studentLeave);
        if (i > 0 && StringUtils.hasLength(studentLeave.getStudentLeaveId()))
            return insertStudentLeaveAuditRecord(studentLeave.getStudentLeaveId());
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> insertStudentLeaveAuditRecord(String studentLeaveId) {
        StudentLeaveAudit studentLeaveAudit = new StudentLeaveAudit();
        studentLeaveAudit.setStudentLeaveId(studentLeaveId);
        int i = studentLeaveAuditMapper.insert(studentLeaveAudit);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentLeave(StudentLeaveDTO studentLeaveDTO) {
        boolean update = UpdateChain.of(StudentLeave.class)
                .set(StudentLeave::getLeaveType, studentLeaveDTO.getLeaveType(), StringUtils.hasLength(studentLeaveDTO.getLeaveType()))
                .set(StudentLeave::getLeaveDate, studentLeaveDTO.getLeaveDate(), !Objects.isNull(studentLeaveDTO.getLeaveDate()))
                .set(StudentLeave::getLeaveDuration, studentLeaveDTO.getLeaveDuration(), !Objects.isNull(studentLeaveDTO.getLeaveDuration()))
                .set(StudentLeave::getLeaveReason, studentLeaveDTO.getLeaveReason(), StringUtils.hasLength(studentLeaveDTO.getLeaveReason()))
                .where(StudentLeave::getStudentId).eq(studentLeaveDTO.getStudentId())
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudentLeave(String studentLeaveId) {
        QueryWrapper wrapper = QueryWrapper.create().where(STUDENT_LEAVE_AUDIT.STUDENT_LEAVE_ID.eq(studentLeaveId));
        int i = studentLeaveAuditMapper.deleteByQuery(wrapper);
        if (i >= 0) {
            int j = mapper.deleteById(studentLeaveId);
            if (j > 0)
                return ResponseUtil.success();
        }
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    public BaseResponse<Page<StudentLeaveItem>> getStudentLeave(LeaveQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        if (!selectStateHandler(query))
            throw new ServiceException(ServiceExceptionEnum.SELECT_NOT_IN);
        Page<StudentLeaveItem> studentLeaveVOPage = QueryChain.of(StudentLeave.class)
                .select(STUDENT.ALL_COLUMNS, STUDENT_LEAVE.ALL_COLUMNS, STUDENT_LEAVE_AUDIT.AUDIT_DATE, MAJOR.ALL_COLUMNS, USER.USERNAME.as("auditorNo"), USER.REAL_NAME.as("auditorName"))
                .from(STUDENT_LEAVE)
                .innerJoin(STUDENT).on(STUDENT.STUDENT_ID.eq(STUDENT_LEAVE.STUDENT_ID))
                .innerJoin(MAJOR).on(STUDENT.MAJOR_ID.eq(MAJOR.MAJOR_ID))
                .leftJoin(STUDENT_LEAVE_AUDIT).on(STUDENT_LEAVE_AUDIT.STUDENT_LEAVE_ID.eq(STUDENT_LEAVE.STUDENT_LEAVE_ID))
                .leftJoin(USER).on(USER.UID.eq(STUDENT_LEAVE_AUDIT.AUDITOR_ID))
                .where(Student::getGradeId).eq(query.getGrade())
                .and(Student::getMajorId).eq(query.getMajorId())
                .and(StudentLeave::getLeaveDate).eq(query.getLeaveDate())
                .and(STUDENT_LEAVE_AUDIT.AUDIT_STATE.eq(query.getAuditState()))
                .pageAs(Page.of(pageNo, pageSize), StudentLeaveItem.class);
        return ResponseUtil.success(studentLeaveVOPage);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> audiStudentLeave(StudentLeaveAuditDTO studentLeaveAuditDTO) {
        if (!updateStateHandler(studentLeaveAuditDTO.getAuditState()))
            throw new ServiceException(ServiceExceptionEnum.SELECT_NOT_IN);
        boolean update = UpdateChain.of(StudentLeaveAudit.class)
                .set(StudentLeaveAudit::getAuditorId, studentLeaveAuditDTO.getAuditorId())
                .set(StudentLeaveAudit::getAuditDate, LocalDate.now())
                .set(StudentLeaveAudit::getAuditState, studentLeaveAuditDTO.getAuditState())
                .where(StudentLeaveAudit::getStudentLeaveId).eq(studentLeaveAuditDTO.getStudentLeaveId())
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    private boolean updateStateHandler(String state) {
        switch (state) {
            case "通过", "拒绝" -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }
    private boolean selectStateHandler(LeaveQuery query) {
        if (!Objects.isNull(query.getAuditState()))
            switch (query.getAuditState()) {
                case "通过", "拒绝", "审核中" -> {
                    return true;
                }
                default -> {
                    return false;
                }
            }
        return true;
    }
}
