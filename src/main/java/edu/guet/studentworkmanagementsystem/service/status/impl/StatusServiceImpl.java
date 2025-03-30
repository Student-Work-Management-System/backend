package edu.guet.studentworkmanagementsystem.service.status.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.status.StudentStatusQuery;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.entity.po.status.Status;
import edu.guet.studentworkmanagementsystem.entity.po.status.StudentStatus;
import edu.guet.studentworkmanagementsystem.entity.vo.status.StudentStatusDetailItem;
import edu.guet.studentworkmanagementsystem.entity.vo.status.StudentStatusItem;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.status.StatusMapper;
import edu.guet.studentworkmanagementsystem.mapper.status.StudentStatusMapper;
import edu.guet.studentworkmanagementsystem.mapper.student.StudentBasicMapper;
import edu.guet.studentworkmanagementsystem.service.status.StatusService;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static edu.guet.studentworkmanagementsystem.entity.po.other.table.GradeTableDef.GRADE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.status.table.StatusTableDef.STATUS;
import static edu.guet.studentworkmanagementsystem.entity.po.status.table.StudentStatusTableDef.STUDENT_STATUS;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentBasicTableDef.STUDENT_BASIC;

@Service
public class StatusServiceImpl extends ServiceImpl<StudentStatusMapper, StudentStatus> implements StatusService {
    @Qualifier("readThreadPool")
    @Autowired
    private ThreadPoolTaskExecutor readThreadPool;
    @Autowired
    private StatusMapper statusMapper;
    @Autowired
    private StudentBasicMapper studentBasicMapper;

    @Override
    public BaseResponse<List<Status>> getAllStatus() {
        CompletableFuture<List<Status>> future =
                CompletableFuture.supplyAsync(() -> statusMapper.selectAll(), readThreadPool);
        List<Status> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> addStatus(Status status) {
        int i = statusMapper.insert(status);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateStatus(Status status) {
        int update = statusMapper.update(status);
        if (update <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStatus(String statusId) {
        List<StudentStatus> studentStatuses = QueryChain.of(StudentStatus.class)
                .where(STUDENT_STATUS.STATUS_ID.eq(statusId))
                .list();
        if (!Objects.isNull(studentStatuses) && !studentStatuses.isEmpty()) {
            int rows = studentStatuses.size();
            Set<String> set = studentStatuses.stream().map(StudentStatus::getStudentStatusId).collect(Collectors.toSet());
            int size = mapper.deleteBatchByIds(set);
            if (rows != size)
                throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        }
        int i = statusMapper.deleteById(statusId);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    public Status getStatus(String statusId) {
        return statusMapper.selectOneById(statusId);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> importStudentStatus(ValidateList<StudentStatus> studentStatuses) {
        int size = studentStatuses.size();
        int i = mapper.insertBatch(studentStatuses);
        if (i != size)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public boolean importStudentStatus(Set<String> studentIds) {
        ArrayList<StudentStatus> studentStatuses = new ArrayList<>();
        for (String studentId : studentIds) {
            StudentStatus studentStatus = createStudentStatusFromStudentService(studentId);
            studentStatuses.add(studentStatus);
        }
        int size = studentStatuses.size();
        int i = mapper.insertBatch(studentStatuses);
        return i == size;
    }

    public StudentStatus createStudentStatusFromStudentService(String studentId) {
        return StudentStatus.builder()
                .studentId(studentId)
                .statusId("1")
                .log("学生入学")
                .modifiedTime(LocalDate.now())
                .build();
    }

    @Override
    @Transactional
    public BaseResponse<Scholarship> insertStudentStatus(StudentStatus studentStatus) {
        int i = mapper.insert(studentStatus);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentStatus(StudentStatus studentStatus) {
        // 需要记录学籍状态变化, 要获取到原先的学籍状态
        StudentStatus origin = getStudentStatus(studentStatus.getStudentStatusId());
        // 创建并添加到数据库中
        StudentStatus newStudentStatus = preUpdateStudentStatus(studentStatus, origin.getStatusId());
        int i = mapper.insert(newStudentStatus);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        // 处理原先的学籍状态
        boolean success = afterUpdateStudentStatus(studentStatus.getStudentStatusId());
        if (!success)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    public StudentStatus getStudentStatus(String studentStatusId) {
        return mapper.selectOneById(studentStatusId);
    }

    public StudentStatus preUpdateStudentStatus(StudentStatus studentStatus, String oldStatusId) {
        String newStatusId = studentStatus.getStatusId();
        Status newStatus = getStatus(newStatusId);
        Status oldStatus = getStatus(oldStatusId);
        String log = "学籍状态: id=" + oldStatus.getStatusId() + ", 名称=" + oldStatus.getStatusName() +
                " -> " + "id=" + newStatus.getStatusId() + ", 名称=" + newStatus.getStatusName();
        return StudentStatus.builder()
                .statusId(newStatusId)
                .studentId(studentStatus.getStudentId())
                .log(log)
                .modifiedTime(studentStatus.getModifiedTime())
                .statusEnabled(true)
                .build();
    }

    public boolean afterUpdateStudentStatus(String oldStudentStatusId) {
        return UpdateChain.of(StudentStatus.class)
                .set(STUDENT_STATUS.STATUS_ENABLED, false)
                .where(STUDENT_STATUS.STUDENT_STATUS_ID.eq(oldStudentStatusId))
                .update();
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
    public BaseResponse<Page<StudentStatusItem>> getAllRecords(StudentStatusQuery query) {
        CompletableFuture<Page<StudentStatusItem>> future = CompletableFuture.supplyAsync(() -> {
            Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(10);
            return QueryChain.of(StudentStatus.class)
                    .select(
                            STUDENT_STATUS.ALL_COLUMNS,
                            STATUS.STATUS_NAME,
                            STUDENT_BASIC.STUDENT_ID,
                            STUDENT_BASIC.NAME,
                            MAJOR.MAJOR_NAME,
                            GRADE.GRADE_NAME
                    )
                    .from(STUDENT_STATUS)
                    .innerJoin(STUDENT_BASIC).on(STUDENT_BASIC.STUDENT_ID.eq(STUDENT_STATUS.STUDENT_ID)
                            .and(STUDENT_STATUS.STATUS_ENABLED.eq(true)))
                    .innerJoin(STATUS).on(STATUS.STATUS_ID.eq(STUDENT_STATUS.STATUS_ID))
                    .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT_BASIC.MAJOR_ID))
                    .innerJoin(GRADE).on(GRADE.GRADE_ID.eq(STUDENT_BASIC.GRADE_ID))
                    .where(
                            STATUS.STATUS_NAME.like(query.getSearch())
                                    .or(STUDENT_BASIC.STUDENT_ID.likeLeft(query.getSearch()))
                                    .or(STUDENT_BASIC.NAME.like(query.getSearch()))
                    )
                    .and(MAJOR.MAJOR_ID.eq(query.getMajorId()))
                    .and(GRADE.GRADE_ID.eq(query.getGradeId()))
                    .pageAs(Page.of(pageNo, pageSize), StudentStatusItem.class);
        }, readThreadPool);
        Page<StudentStatusItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    public BaseResponse<StudentStatusDetailItem> getStudentStatusDetail(String studentId) {
        CompletableFuture<StudentStatusDetailItem> future = CompletableFuture.supplyAsync(() -> QueryChain.of(StudentStatus.class)
                .where(STUDENT_BASIC.STUDENT_ID.eq(studentId))
                .oneAs(StudentStatusDetailItem.class), readThreadPool);
        StudentStatusDetailItem execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

}
