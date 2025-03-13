package edu.guet.studentworkmanagementsystem.service.status.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.status.StatusList;
import edu.guet.studentworkmanagementsystem.entity.dto.status.StatusQuery;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.entity.po.status.Status;
import edu.guet.studentworkmanagementsystem.entity.po.status.StudentStatus;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import edu.guet.studentworkmanagementsystem.entity.vo.status.StudentStatusVO;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.status.StatusMapper;
import edu.guet.studentworkmanagementsystem.mapper.status.StudentStatusMapper;
import edu.guet.studentworkmanagementsystem.service.status.StatusService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static edu.guet.studentworkmanagementsystem.entity.po.status.table.StudentStatusTableDef.STUDENT_STATUS;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentTableDef.STUDENT;

@Service
public class StatusServiceImpl extends ServiceImpl<StudentStatusMapper, StudentStatus> implements StatusService {
    @Qualifier("readThreadPool")
    @Autowired
    private ThreadPoolTaskExecutor readThreadPool;
    @Autowired
    private StatusMapper statusMapper;

    @Override
    public BaseResponse<List<Status>> getAllStatus() {
        CompletableFuture<BaseResponse<List<Status>>> future = CompletableFuture.supplyAsync(() -> ResponseUtil.success(statusMapper.selectAll()), readThreadPool);
        try {
            return future.get(3, TimeUnit.SECONDS);
        } catch (Exception exception) {
            Throwable cause = exception.getCause();
            switch (cause) {
                case ServiceException serviceException ->
                        throw serviceException;
                case TimeoutException ignored ->
                        throw new ServiceException(ServiceExceptionEnum.GET_RESOURCE_TIMEOUT);
                case InterruptedException ignored ->
                        throw new ServiceException(ServiceExceptionEnum.GET_RESOURCE_INTERRUPTED);
                default ->
                        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
            }
        }
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> importStudentStatus(StatusList statusList) {
        List<StudentStatus> studentStatuses = statusList.getStatuses();
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
                .set(StudentStatus::getHandle, studentStatus.getHandle(), StringUtils.hasLength(studentStatus.getHandle()))
                .set(StudentStatus::getChangedDate, LocalDate.now())
                .set(StudentStatus::getStudentId, studentStatus.getStudentId(), StringUtils::hasLength)
                .where(StudentStatus::getStudentStatusId).eq(studentStatus.getStudentStatusId())
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
