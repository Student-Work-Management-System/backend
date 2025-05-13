package edu.guet.studentworkmanagementsystem.service.leave.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.Common;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.AuditLeaveQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.LeaveStatQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.StudentLeaveQuery;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeave;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeaveEvidence;
import edu.guet.studentworkmanagementsystem.entity.vo.leave.StudentLeaveItem;
import edu.guet.studentworkmanagementsystem.entity.vo.leave.StudentLeaveStatGroup;
import edu.guet.studentworkmanagementsystem.entity.vo.leave.StudentLeaveStatItem;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.leave.StudentLeaveMapper;
import edu.guet.studentworkmanagementsystem.securiy.SystemAuthority;
import edu.guet.studentworkmanagementsystem.service.leave.StudentLeaveService;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import edu.guet.studentworkmanagementsystem.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.mybatisflex.core.query.QueryMethods.dateDiff;
import static edu.guet.studentworkmanagementsystem.entity.po.leave.table.StudentLeaveAuditTableDef.STUDENT_LEAVE_AUDIT;
import static edu.guet.studentworkmanagementsystem.entity.po.leave.table.StudentLeaveEvidenceTableDef.STUDENT_LEAVE_EVIDENCE;
import static edu.guet.studentworkmanagementsystem.entity.po.leave.table.StudentLeaveTableDef.STUDENT_LEAVE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.GradeTableDef.GRADE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentBasicTableDef.STUDENT_BASIC;

@Service
public class StudentLeaveServiceImpl extends ServiceImpl<StudentLeaveMapper, StudentLeave> implements StudentLeaveService {

    private final ThreadPoolTaskExecutor readThreadPool;

    public StudentLeaveServiceImpl(@Qualifier("readThreadPool") ThreadPoolTaskExecutor readThreadPool) {
        this.readThreadPool = readThreadPool;
    }

    @Override
    @Transactional
    public String addStudentLeave(StudentLeave studentLeave) {
        int i = mapper.insert(studentLeave);
        if (i == 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return studentLeave.getLeaveId();
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

    @Override
    public BaseResponse<Page<StudentLeaveItem>> getOwnLeaves(StudentLeaveQuery query) {
        String studentId = SecurityUtil.getUserCredentials().getUsername();
        CompletableFuture<Page<StudentLeaveItem>> future = CompletableFuture.supplyAsync(() -> {
            int pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
            int pageSize = Optional.ofNullable(query.getPageSize()).orElse(10);
            QueryCondition condition = null;
            if (Objects.nonNull(query.getState()) && Objects.nonNull(query.getNeedLeader())) {
                condition = createCondition(query.getState(), query.getNeedLeader());
            }
            Page<StudentLeaveItem> items = QueryChain.of(StudentLeave.class)
                    .select(
                            STUDENT_LEAVE.ALL_COLUMNS,
                            STUDENT_LEAVE_AUDIT.ALL_COLUMNS,
                            STUDENT_BASIC.NAME,
                            MAJOR.MAJOR_NAME,
                            GRADE.GRADE_NAME
                    )
                    .innerJoin(STUDENT_LEAVE_AUDIT).on(STUDENT_LEAVE_AUDIT.LEAVE_ID.eq(STUDENT_LEAVE.LEAVE_ID))
                    .innerJoin(STUDENT_BASIC).on(STUDENT_BASIC.STUDENT_ID.eq(STUDENT_LEAVE.STUDENT_ID))
                    .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT_BASIC.MAJOR_ID))
                    .innerJoin(GRADE).on(GRADE.GRADE_ID.eq(STUDENT_BASIC.GRADE_ID))
                    .where(STUDENT_LEAVE.STUDENT_ID.eq(studentId))
                    .and(STUDENT_LEAVE.TYPE.eq(query.getType()))
                    .and(condition)
                    .pageAs(Page.of(pageNo, pageSize), StudentLeaveItem.class);
            items.getRecords().forEach(it -> {
                String leaveId = it.getLeaveId();
                List<StudentLeaveEvidence> evidenceList = QueryChain.of(StudentLeaveEvidence.class)
                        .where(STUDENT_LEAVE_EVIDENCE.LEAVE_ID.eq(leaveId))
                        .list();
                List<String> evidences = evidenceList.stream().map(StudentLeaveEvidence::getPath).toList();
                it.setEvidences(evidences);
            });
            return items;
        }, readThreadPool);
        Page<StudentLeaveItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    public QueryCondition createCondition(String state, Boolean needLeader) {
        /*
          最终状态为: 审核中
          请假天数 > 7
            辅导员审核通过, 副书记审核审核中 -> 审核中
            辅导员状态审核中 -> 审核中
          请假天数 <= 7
            辅导员状态审核中 -> 审核中
         */
        if (Common.WAITING.getValue().equals(state)) {
            return needLeader
                    ? dateDiff(STUDENT_LEAVE.END_DAY, STUDENT_LEAVE.START_DAY).gt(7)
                    .and(
                            STUDENT_LEAVE_AUDIT.COUNSELOR_HANDLE_STATE.eq(Common.PASS.getValue())
                                    .and(STUDENT_LEAVE_AUDIT.LEADER_HANDLE_STATE.eq(Common.WAITING.getValue()))
                                    .or(STUDENT_LEAVE_AUDIT.COUNSELOR_HANDLE_STATE.eq(Common.WAITING))
                    )
                    : dateDiff(STUDENT_LEAVE.END_DAY, STUDENT_LEAVE.START_DAY).le(7)
                    .and(STUDENT_LEAVE_AUDIT.COUNSELOR_HANDLE_STATE.eq(Common.WAITING.getValue()));
        }
        /*
          最终状态为: 拒绝
          请假天数 > 7
            辅导员审核通过, 副书记审核拒绝 -> 拒绝
            辅导员状态拒绝 -> 拒绝
          请假天数 <= 7
            辅导员状态拒绝 -> 拒绝
         */
        if (Common.REJECT.getValue().equals(state)) {
            return needLeader
                    ? dateDiff(STUDENT_LEAVE.END_DAY, STUDENT_LEAVE.START_DAY).gt(7)
                    .and(
                            STUDENT_LEAVE_AUDIT.COUNSELOR_HANDLE_STATE.eq(Common.PASS.getValue())
                                    .and(STUDENT_LEAVE_AUDIT.LEADER_HANDLE_STATE.eq(Common.REJECT.getValue()))
                                    .or(STUDENT_LEAVE_AUDIT.COUNSELOR_HANDLE_STATE.eq(Common.REJECT.getValue()))
                    )
                    : dateDiff(STUDENT_LEAVE.END_DAY, STUDENT_LEAVE.START_DAY).le(7)
                    .and(STUDENT_LEAVE_AUDIT.COUNSELOR_HANDLE_STATE.eq(Common.REJECT));
        }
        /*
          最终状态为: 通过
          请假天数 > 7
            辅导员审核通过, 副书记审核通过 -> 通过
          请假天数 <= 7
            辅导员状态通过 -> 通过
         */
        return needLeader
                ? dateDiff(STUDENT_LEAVE.END_DAY, STUDENT_LEAVE.START_DAY).gt(7)
                .and(STUDENT_LEAVE_AUDIT.COUNSELOR_HANDLE_STATE.eq(Common.PASS.getValue())
                        .and(STUDENT_LEAVE_AUDIT.LEADER_HANDLE_STATE.eq(Common.PASS.getValue())))
                : dateDiff(STUDENT_LEAVE.END_DAY, STUDENT_LEAVE.START_DAY).le(7)
                .and(STUDENT_LEAVE_AUDIT.COUNSELOR_HANDLE_STATE.eq(Common.PASS.getValue()));
    }

    @Override
    public BaseResponse<Page<StudentLeaveItem>> getLeaves(AuditLeaveQuery query) {
        QueryCondition condition = getCondition(query);
        CompletableFuture<Page<StudentLeaveItem>> future = CompletableFuture.supplyAsync(() -> {
            int pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
            int pageSize = Optional.ofNullable(query.getPageSize()).orElse(10);
            Page<StudentLeaveItem> items = QueryChain.of(StudentLeave.class)
                    .select(
                            STUDENT_LEAVE.ALL_COLUMNS,
                            STUDENT_LEAVE_AUDIT.ALL_COLUMNS,
                            STUDENT_BASIC.NAME,
                            MAJOR.MAJOR_NAME,
                            GRADE.GRADE_NAME
                    )
                    .innerJoin(STUDENT_LEAVE_AUDIT).on(STUDENT_LEAVE_AUDIT.LEAVE_ID.eq(STUDENT_LEAVE.LEAVE_ID))
                    .innerJoin(STUDENT_BASIC).on(STUDENT_BASIC.STUDENT_ID.eq(STUDENT_LEAVE.STUDENT_ID))
                    .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT_BASIC.MAJOR_ID))
                    .innerJoin(GRADE).on(GRADE.GRADE_ID.eq(STUDENT_BASIC.GRADE_ID))
                    .where(
                            STUDENT_BASIC.STUDENT_ID.likeLeft(query.getSearch())
                                    .or(STUDENT_BASIC.NAME.likeLeft(query.getSearch()))
                    )
                    .and(MAJOR.MAJOR_ID.eq(query.getMajorId()))
                    .and(GRADE.GRADE_ID.eq(query.getGradeId()))
                    .and(dateDiff(STUDENT_LEAVE.START_DAY, STUDENT_LEAVE.END_DAY).eq(query.getTotalDay()))
                    .and(STUDENT_LEAVE.TYPE.eq(query.getType()))
                    .and(STUDENT_LEAVE.DESTROYED.eq(query.getDestroyed()))
                    .and(condition)
                    .pageAs(Page.of(pageNo, pageSize), StudentLeaveItem.class);
            items.getRecords().forEach(it -> {
                String leaveId = it.getLeaveId();
                List<StudentLeaveEvidence> evidenceList = QueryChain.of(StudentLeaveEvidence.class)
                        .where(STUDENT_LEAVE_EVIDENCE.LEAVE_ID.eq(leaveId))
                        .list();
                List<String> evidences = evidenceList.stream().map(StudentLeaveEvidence::getPath).toList();
                it.setEvidences(evidences);
            });
            return items;
        }, readThreadPool);
        Page<StudentLeaveItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    /**
     * 根据用户所拥有的权限生成SQL查询条件
     * <br/>
     * 有辅导员批假权限 -> 能查询到指派到该辅导员的请假申请
     * <br/>
     * 有更高级领导权限(副书记) -> 成查询到辅导员已经批准的请假请求
     * <br/>
     * 同时, 还需要根据审核状态来过滤
     */
    public QueryCondition getCondition(AuditLeaveQuery query) {
        List<SystemAuthority> authorities = SecurityUtil.getUserAuthorities();
        String username = SecurityUtil.getUserCredentials().getUsername();
        boolean hasCounselorPermission = authorities.stream()
                .map(SystemAuthority::getAuthority)
                .anyMatch(Common.LEAVE_COUNSELOR_PERMISSION.getValue()::equals);
        boolean hasLeaderPermission = authorities.stream()
                .map(SystemAuthority::getAuthority)
                .anyMatch(Common.LEAVE_LEADER_PERMISSION.getValue()::equals);
        if (hasCounselorPermission && !hasLeaderPermission)
            return dateDiff(STUDENT_LEAVE.END_DAY, STUDENT_LEAVE.START_DAY).le(7)
                    .and(STUDENT_LEAVE_AUDIT.COUNSELOR_ID.eq(username))
                    .and(STUDENT_LEAVE_AUDIT.COUNSELOR_HANDLE_STATE.eq(query.getCounselorHandleState()));
        else if (hasLeaderPermission && !hasCounselorPermission)
            return dateDiff(STUDENT_LEAVE.END_DAY, STUDENT_LEAVE.START_DAY).gt(7)
                    .and(STUDENT_LEAVE_AUDIT.COUNSELOR_HANDLE_STATE.eq(Common.PASS.getValue()))
                    .and(STUDENT_LEAVE_AUDIT.LEADER_HANDLE_STATE.eq(query.getLeaderHandleState()));
        return null;
    }


    @Override
    public BaseResponse<List<StudentLeaveStatGroup>> getStat(LeaveStatQuery query) {
        CompletableFuture<List<StudentLeaveStatGroup>> future = CompletableFuture.supplyAsync(() -> {
            List<StudentLeaveStatItem> raws = getRawData(query);
            return raw2Group(raws);
        }, readThreadPool);
        List<StudentLeaveStatGroup> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    public List<StudentLeaveStatItem> getRawData(LeaveStatQuery query) {
        return QueryChain.of(StudentLeave.class)
                .select(
                        MAJOR.MAJOR_NAME,
                        GRADE.GRADE_NAME,
                        STUDENT_LEAVE.TYPE,
                        STUDENT_LEAVE.INTERNSHIP,
                        STUDENT_LEAVE.DESTROYED
                )
                .from(STUDENT_LEAVE)
                .innerJoin(STUDENT_BASIC).on(STUDENT_BASIC.STUDENT_ID.eq(STUDENT_LEAVE.STUDENT_ID))
                .innerJoin(GRADE).on(GRADE.GRADE_ID.eq(STUDENT_BASIC.GRADE_ID))
                .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT_BASIC.MAJOR_ID))
                .innerJoin(STUDENT_LEAVE_AUDIT).on(STUDENT_LEAVE_AUDIT.LEAVE_ID.eq(STUDENT_LEAVE.LEAVE_ID))
                .where(STUDENT_BASIC.ENABLED.eq(true))
                .and(GRADE.GRADE_ID.eq(query.getGradeId()))
                .and(MAJOR.MAJOR_ID.eq(query.getMajorId()))
                .and(
                        dateDiff(STUDENT_LEAVE.END_DAY, STUDENT_LEAVE.START_DAY).lt(7)
                                .and(STUDENT_LEAVE_AUDIT.COUNSELOR_HANDLE_STATE.eq(Common.PASS.getValue()))
                                .or(
                                        dateDiff(STUDENT_LEAVE.END_DAY, STUDENT_LEAVE.START_DAY).ge(7)
                                                .and(STUDENT_LEAVE_AUDIT.COUNSELOR_HANDLE_STATE.eq(Common.PASS.getValue()))
                                                .and(STUDENT_LEAVE_AUDIT.LEADER_HANDLE_STATE.eq(Common.PASS.getValue()))
                                )
                )
                .listAs(StudentLeaveStatItem.class);
    }
    public List<StudentLeaveStatGroup> raw2Group(List<StudentLeaveStatItem> items) {
        Map<String, Map<String, Map<String, List<StudentLeaveStatItem>>>> grouped =
                items.stream().collect(Collectors.groupingBy(
                        StudentLeaveStatItem::getGradeName,
                        Collectors.groupingBy(
                                StudentLeaveStatItem::getMajorName,
                                Collectors.groupingBy(StudentLeaveStatItem::getType)
                        )
                ));

        List<StudentLeaveStatGroup> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, Map<String, List<StudentLeaveStatItem>>>> gradeEntry : grouped.entrySet()) {
            String grade = gradeEntry.getKey();
            List<StudentLeaveStatGroup.MajorGroup> majorGroups = new ArrayList<>();

            for (Map.Entry<String, Map<String, List<StudentLeaveStatItem>>> majorEntry : gradeEntry.getValue().entrySet()) {
                String major = majorEntry.getKey();
                List<StudentLeaveStatGroup.StudentLeaveStat> leaveStats = new ArrayList<>();

                for (Map.Entry<String, List<StudentLeaveStatItem>> typeEntry : majorEntry.getValue().entrySet()) {
                    String type = typeEntry.getKey();
                    List<StudentLeaveStatItem> statItems = typeEntry.getValue();

                    long destroyedCount = statItems.stream().filter(StudentLeaveStatItem::isDestroyed).count();
                    long internshipCount = statItems.stream().filter(StudentLeaveStatItem::isInternship).count();
                    long totalCount = statItems.size();

                    leaveStats.add(new StudentLeaveStatGroup.StudentLeaveStat(
                            type,
                            String.valueOf(destroyedCount),
                            String.valueOf(internshipCount),
                            String.valueOf(totalCount)
                    ));
                }

                majorGroups.add(new StudentLeaveStatGroup.MajorGroup(major, leaveStats));
            }

            result.add(new StudentLeaveStatGroup(grade, majorGroups));
        }

        return result;
    }

}
