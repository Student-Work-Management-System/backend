package edu.guet.studentworkmanagementsystem.service.punlishment.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.punishment.StudentPunishmentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.punishment.StudentPunishmentStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.punishment.StudentPunishment;
import edu.guet.studentworkmanagementsystem.entity.vo.punishment.StudentPunishmentItem;
import edu.guet.studentworkmanagementsystem.entity.vo.punishment.StudentPunishmentStatGroup;
import edu.guet.studentworkmanagementsystem.entity.vo.punishment.StudentPunishmentStatRow;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.punishment.StudentPunishmentMapper;
import edu.guet.studentworkmanagementsystem.service.punlishment.StudentPunishmentService;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static edu.guet.studentworkmanagementsystem.entity.po.other.table.GradeTableDef.GRADE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.punishment.table.StudentPunishmentTableDef.STUDENT_PUNISHMENT;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentBasicTableDef.STUDENT_BASIC;

@Service
public class StudentPunishmentServiceImpl extends ServiceImpl<StudentPunishmentMapper, StudentPunishment> implements StudentPunishmentService {

    private final ThreadPoolTaskExecutor readThreadPool;

    public StudentPunishmentServiceImpl(@Qualifier("readThreadPool") ThreadPoolTaskExecutor readThreadPool) {
        this.readThreadPool = readThreadPool;
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> importStudentPunishment(ValidateList<StudentPunishment> studentPunishments) {
        int size = studentPunishments.size();
        int i = mapper.insertBatch(studentPunishments);
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<Page<StudentPunishmentItem>> getStudentPunishments(StudentPunishmentQuery query) {
        CompletableFuture<Page<StudentPunishmentItem>> future = CompletableFuture.supplyAsync(() -> {
            int pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
            int pageSize = Optional.ofNullable(query.getPageSize()).orElse(10);
            return QueryChain.of(StudentPunishment.class)
                    .select(
                            STUDENT_PUNISHMENT.ALL_COLUMNS,
                            STUDENT_BASIC.ALL_COLUMNS,
                            MAJOR.ALL_COLUMNS,
                            GRADE.ALL_COLUMNS
                    )
                    .from(STUDENT_PUNISHMENT)
                    .innerJoin(STUDENT_BASIC).on(STUDENT_PUNISHMENT.STUDENT_ID.eq(STUDENT_BASIC.STUDENT_ID))
                    .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT_BASIC.MAJOR_ID))
                    .innerJoin(GRADE).on(GRADE.GRADE_ID.eq(STUDENT_BASIC.GRADE_ID))
                    .where(
                            STUDENT_BASIC.NAME.like(query.getSearch())
                                    .or(STUDENT_BASIC.STUDENT_ID.likeLeft(query.getSearch()))
                    )
                    .and(STUDENT_PUNISHMENT.LEVEL.eq(query.getLevel()))
                    .and(MAJOR.MAJOR_ID.eq(query.getMajorId()))
                    .and(GRADE.GRADE_ID.eq(query.getGradeId()))
                    .pageAs(Page.of(pageNo, pageSize), StudentPunishmentItem.class);
        }, readThreadPool);
        Page<StudentPunishmentItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudentPunishment(String studentPunishmentId) {
        int i = mapper.deleteById(studentPunishmentId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentPunishment(StudentPunishment studentPunishment) {
        boolean update = UpdateChain.of(StudentPunishment.class)
                .set(STUDENT_PUNISHMENT.LEVEL, studentPunishment.getLevel(), StringUtils::hasLength)
                .set(STUDENT_PUNISHMENT.REASON, studentPunishment.getReason(), StringUtils::hasLength)
                .set(STUDENT_PUNISHMENT.DATE, LocalDate.now())
                .where(STUDENT_PUNISHMENT.STUDENT_PUNISHMENT_ID.eq(studentPunishment.getStudentPunishmentId()))
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<List<StudentPunishmentStatGroup>> getStat(StudentPunishmentStatQuery query) {
        CompletableFuture<List<StudentPunishmentStatGroup>> future = CompletableFuture.supplyAsync(() -> {
            List<StudentPunishmentStatRow> rows = mapper.getStat(query);
            return convertToStatGroup(rows);
        }, readThreadPool);
        List<StudentPunishmentStatGroup> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    public List<StudentPunishmentStatGroup> convertToStatGroup(List<StudentPunishmentStatRow> rows) {
        Map<String, List<StudentPunishmentStatRow>> gradeMap = rows.stream()
                .collect(Collectors.groupingBy(StudentPunishmentStatRow::getGradeName));

        List<StudentPunishmentStatGroup> statGroups = new ArrayList<>();

        for (Map.Entry<String, List<StudentPunishmentStatRow>> entry : gradeMap.entrySet()) {
            String gradeName = entry.getKey();
            List<StudentPunishmentStatRow> gradeRows = entry.getValue();

            Map<String, List<StudentPunishmentStatRow>> majorMap = gradeRows.stream()
                    .collect(Collectors.groupingBy(StudentPunishmentStatRow::getMajorName));

            List<StudentPunishmentStatGroup.MajorGroup> majorGroups = new ArrayList<>();

            for (Map.Entry<String, List<StudentPunishmentStatRow>> majorEntry : majorMap.entrySet()) {
                String majorName = majorEntry.getKey();
                List<StudentPunishmentStatRow> majorRows = majorEntry.getValue();

                // 使用处分类型（punishmentName）进行分组
                Map<String, Long> punishmentMap = majorRows.stream()
                        .collect(Collectors.groupingBy(StudentPunishmentStatRow::getPunishmentName, Collectors.counting()));

                List<StudentPunishmentStatGroup.PunishmentGroup> punishmentGroups = punishmentMap.entrySet().stream()
                        .map(entry1 -> new StudentPunishmentStatGroup.PunishmentGroup(entry1.getKey(), entry1.getValue().toString()))
                        .collect(Collectors.toList());

                majorGroups.add(new StudentPunishmentStatGroup.MajorGroup(majorName, punishmentGroups));
            }

            statGroups.add(new StudentPunishmentStatGroup(gradeName, majorGroups));
        }

        return statGroups;
    }
}
