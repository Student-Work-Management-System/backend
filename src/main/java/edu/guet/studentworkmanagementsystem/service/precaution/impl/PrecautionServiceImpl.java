package edu.guet.studentworkmanagementsystem.service.precaution.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.precaution.PrecautionQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.precaution.PrecautionStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.precaution.StudentPrecaution;
import edu.guet.studentworkmanagementsystem.entity.vo.precaution.PrecautionStatRow;
import edu.guet.studentworkmanagementsystem.entity.vo.precaution.StudentPrecautionItem;
import edu.guet.studentworkmanagementsystem.entity.vo.precaution.StudentPrecautionStatGroup;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.precaution.PrecautionMapper;
import edu.guet.studentworkmanagementsystem.service.precaution.PrecautionService;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import edu.guet.studentworkmanagementsystem.utils.SecurityUtil;
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
import static edu.guet.studentworkmanagementsystem.entity.po.precaution.table.StudentPrecautionTableDef.STUDENT_PRECAUTION;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentBasicTableDef.STUDENT_BASIC;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.UserTableDef.USER;

@Service
public class PrecautionServiceImpl extends ServiceImpl<PrecautionMapper, StudentPrecaution> implements PrecautionService {

    private final ThreadPoolTaskExecutor readThreadPool;

    public PrecautionServiceImpl(@Qualifier("readThreadPool") ThreadPoolTaskExecutor readThreadPool) {
        this.readThreadPool = readThreadPool;
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> importPrecaution(ValidateList<StudentPrecaution> precautions) {
        ArrayList<StudentPrecaution> studentPrecautions = new ArrayList<>();
        precautions.forEach(it -> {
            StudentPrecaution studentPrecaution = insertHandler(it);
            studentPrecautions.add(studentPrecaution);
        });
        int i = mapper.insertBatch(studentPrecautions);
        int size = studentPrecautions.size();
        if (i != size)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> insertPrecaution(StudentPrecaution precaution) {
        StudentPrecaution studentPrecaution = insertHandler(precaution);
        int i = mapper.insert(studentPrecaution);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    public StudentPrecaution insertHandler(StudentPrecaution precaution) {
        precaution.setCreatedAt(LocalDate.now());
        precaution.setStatus(false);
        return precaution;
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updatePrecaution(StudentPrecaution precaution) {
        boolean update = UpdateChain.of(StudentPrecaution.class)
                .set(STUDENT_PRECAUTION.LEVEL_CODE, precaution.getLevelCode(), StringUtils::hasLength)
                .set(STUDENT_PRECAUTION.TERM, precaution.getTerm(), StringUtils::hasLength)
                .set(STUDENT_PRECAUTION.DETAIL, precaution.getDetail(), StringUtils::hasLength)
                .set(STUDENT_PRECAUTION.UPDATED_AT, LocalDate.now())
                .where(STUDENT_PRECAUTION.STUDENT_PRECAUTION_ID.eq(precaution.getStudentPrecautionId()))
                .update();
        if (!update)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deletePrecaution(String studentPrecautionId) {
        int i = mapper.deleteById(studentPrecautionId);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> handlePrecaution(String studentPrecautionId) {
        String uid = SecurityUtil.getUserPrincipal();
        boolean update = UpdateChain.of(StudentPrecaution.class)
                .set(STUDENT_PRECAUTION.HANDLER_ID, uid)
                .set(STUDENT_PRECAUTION.STATUS, true)
                .set(STUDENT_PRECAUTION.HANDLED_AT, LocalDate.now())
                .where(STUDENT_PRECAUTION.STUDENT_PRECAUTION_ID.eq(studentPrecautionId))
                .update();
        if (!update)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    public BaseResponse<Page<StudentPrecautionItem>> getAllRecords(PrecautionQuery query) {
        CompletableFuture<Page<StudentPrecautionItem>> future = CompletableFuture.supplyAsync(() -> {
            int pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
            int pageSize = Optional.ofNullable(query.getPageSize()).orElse(10);
            return QueryChain.of(StudentPrecaution.class)
                    .select(
                            STUDENT_PRECAUTION.ALL_COLUMNS,
                            GRADE.GRADE_NAME,
                            MAJOR.MAJOR_NAME,
                            USER.REAL_NAME.as("handlerName"),
                            STUDENT_BASIC.ALL_COLUMNS
                    )
                    .from(STUDENT_PRECAUTION)
                    .innerJoin(STUDENT_BASIC).on(STUDENT_BASIC.STUDENT_ID.eq(STUDENT_PRECAUTION.STUDENT_ID))
                    .innerJoin(GRADE).on(GRADE.GRADE_ID.eq(STUDENT_BASIC.GRADE_ID))
                    .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT_BASIC.MAJOR_ID))
                    .leftJoin(USER).on(USER.UID.eq(STUDENT_PRECAUTION.HANDLER_ID))
                    .where(
                            STUDENT_BASIC.STUDENT_ID.likeLeft(query.getSearch())
                                    .or(STUDENT_BASIC.NAME.likeLeft(query.getSearch()))
                    )
                    .and(STUDENT_PRECAUTION.DETAIL.like(query.getDetailSearch()))
                    .and(STUDENT_PRECAUTION.TERM.ge(query.getStartTerm()))
                    .and(STUDENT_PRECAUTION.TERM.le(query.getEndTerm()))
                    .and(STUDENT_PRECAUTION.LEVEL_CODE.eq(query.getLevelCode()))
                    .and(STUDENT_BASIC.GRADE_ID.eq(query.getGradeId()))
                    .and(STUDENT_BASIC.MAJOR_ID.eq(query.getMajorId()))
                    .pageAs(Page.of(pageNo, pageSize), StudentPrecautionItem.class);
        }, readThreadPool);
        Page<StudentPrecautionItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    public BaseResponse<List<StudentPrecautionStatGroup>> getStat(PrecautionStatQuery query) {
        CompletableFuture<List<StudentPrecautionStatGroup>> future = CompletableFuture.supplyAsync(() -> {
            List<PrecautionStatRow> rows = mapper.getStat(query);
            return convertToStatGroups(rows);
        }, readThreadPool);
        List<StudentPrecautionStatGroup> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    private List<StudentPrecautionStatGroup> convertToStatGroups(List<PrecautionStatRow> rows) {
        Map<String, Map<String, Map<String, List<PrecautionStatRow>>>> grouped =
                rows.stream().collect(Collectors.groupingBy(
                        PrecautionStatRow::getGradeName,
                        Collectors.groupingBy(
                                PrecautionStatRow::getMajorName,
                                Collectors.groupingBy(
                                        PrecautionStatRow::getTerm
                                )
                        )
                ));

        return grouped.entrySet().stream().map(gradeEntry -> {
            String gradeName = gradeEntry.getKey();
            Map<String, Map<String, List<PrecautionStatRow>>> majorMap = gradeEntry.getValue();

            List<StudentPrecautionStatGroup.MajorGroup> majors = majorMap.entrySet().stream().map(majorEntry -> {
                String majorName = majorEntry.getKey();
                Map<String, List<PrecautionStatRow>> termMap = majorEntry.getValue();

                List<StudentPrecautionStatGroup.TermGroup> terms = termMap.entrySet().stream().map(termEntry -> {
                    String term = termEntry.getKey();
                    List<PrecautionStatRow> levelRows = termEntry.getValue();

                    List<StudentPrecautionStatGroup.LevelGroup> levels = levelRows.stream()
                            .map(row -> StudentPrecautionStatGroup.LevelGroup.builder()
                                    .levelCode(row.getLevelCode())
                                    .handledNumber(row.getHandledNumber())
                                    .allNumber(row.getAllNumber())
                                    .build())
                            .collect(Collectors.toList());

                    return StudentPrecautionStatGroup.TermGroup.builder()
                            .term(term)
                            .levels(levels)
                            .build();
                }).collect(Collectors.toList());

                return StudentPrecautionStatGroup.MajorGroup.builder()
                        .majorName(majorName)
                        .terms(terms)
                        .build();
            }).collect(Collectors.toList());

            return StudentPrecautionStatGroup.builder()
                    .gradeName(gradeName)
                    .majors(majors)
                    .build();
        }).collect(Collectors.toList());
    }

}
