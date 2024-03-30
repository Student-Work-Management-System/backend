package edu.guet.studentworkmanagementsystem.service.status.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.status.StatusQuery;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.entity.po.status.StudentStatus;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import edu.guet.studentworkmanagementsystem.entity.vo.status.StudentStatusVO;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.status.StudentStatusMapper;
import edu.guet.studentworkmanagementsystem.service.status.StatusService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static edu.guet.studentworkmanagementsystem.entity.po.status.table.StudentStatusTableDef.STUDENT_STATUS;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentTableDef.STUDENT;

@Service
public class StatusServiceImpl extends ServiceImpl<StudentStatusMapper, StudentStatus> implements StatusService {
    @Override
    @Transactional
    public <T> BaseResponse<T> importStudentStatus(List<StudentStatus> studentStatuses) {
        int size = studentStatuses.size();
        int i = mapper.insertBatch(studentStatuses);
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public BaseResponse<Scholarship> insertStudentStatus(StudentStatus studentStatus) {
        int i = mapper.insert(studentStatus);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentStatus(StudentStatus studentStatus) {
        if (Objects.isNull(studentStatus.getStudentStatusId()) || Objects.isNull(studentStatus.getStudentId()))
            throw new ServiceException(ServiceExceptionEnum.KEY_ARGUMENT_NOT_INPUT);
        boolean update = UpdateChain.of(StudentStatus.class)
                .set(StudentStatus::getState, studentStatus.getState(), StringUtils.hasLength(studentStatus.getState()))
                .set(StudentStatus::getHandle, studentStatus.getHandle(), StringUtils.hasLength(studentStatus.getHandle()))
                .set(StudentStatus::getChangedDate, LocalDate.now())
                .where(StudentStatus::getStudentStatusId).eq(studentStatus.getStudentStatusId())
                .where(StudentStatus::getStudentId).eq(studentStatus.getStudentId())
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudentStatus(String studentStatusId) {
        int i = mapper.deleteById(studentStatusId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    public BaseResponse<Page<StudentStatusVO>> getAllRecords(StatusQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        List<String> studentIds = getStudentIds(query);
        Page<StudentStatusVO> studentStatusPage = QueryChain.of(StudentStatus.class)
                .select(STUDENT.ALL_COLUMNS, STUDENT_STATUS.ALL_COLUMNS)
                .from(STUDENT_STATUS)
                .innerJoin(STUDENT).on(STUDENT.STUDENT_ID.eq(STUDENT_STATUS.STUDENT_ID))
                .where(StudentStatus::getState).eq(query.getState())
                .and(StudentStatus::getHandle).eq(query.getHandle())
                .and(STUDENT.STUDENT_ID.in(studentIds))
                .pageAs(Page.of(pageNo, pageSize), StudentStatusVO.class);
        return ResponseUtil.success(studentStatusPage);
    }
    private List<String> getStudentIds(StatusQuery query) {
        List<Student> students = QueryChain.of(Student.class)
                .select(STUDENT.STUDENT_ID)
                .from(STUDENT)
                .where(Student::getMajorId).eq(query.getMajorId())
                .and(Student::getGrade).eq(query.getGrade())
                .list();
        return students.stream().map(Student::getStudentId).collect(Collectors.toList());
    }
}
