package edu.guet.studentworkmanagementsystem.service.povertyAssistance.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance.*;
import edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.PovertyAssistance;
import edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.StudentPovertyAssistance;
import edu.guet.studentworkmanagementsystem.entity.vo.povertyAssistance.PovertyAssistanceStatGroup;
import edu.guet.studentworkmanagementsystem.entity.vo.povertyAssistance.PovertyAssistanceStatRow;
import edu.guet.studentworkmanagementsystem.entity.vo.povertyAssistance.StudentPovertyAssistanceItem;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.povertyAssistance.PovertyAssistanceMapper;
import edu.guet.studentworkmanagementsystem.mapper.povertyAssistance.StudentPovertyAssistanceMapper;
import edu.guet.studentworkmanagementsystem.service.povertyAssistance.PovertyAssistanceService;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static edu.guet.studentworkmanagementsystem.entity.po.other.table.GradeTableDef.GRADE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.table.PovertyAssistanceTableDef.POVERTY_ASSISTANCE;
import static edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.table.StudentPovertyAssistanceTableDef.STUDENT_POVERTY_ASSISTANCE;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentBasicTableDef.STUDENT_BASIC;

@Service
public class PovertyAssistanceServiceImpl extends ServiceImpl<StudentPovertyAssistanceMapper, StudentPovertyAssistance> implements PovertyAssistanceService {
    @Autowired
    private PovertyAssistanceMapper povertyAssistanceMapper;
    @Qualifier("readThreadPool")
    @Autowired
    private ThreadPoolTaskExecutor readThreadPool;
    @Override
    public BaseResponse<List<PovertyAssistance>> getAllPovertyAssistance() {
        CompletableFuture<List<PovertyAssistance>> future =
                CompletableFuture.supplyAsync(() -> povertyAssistanceMapper.selectAll(), readThreadPool);
        List<PovertyAssistance> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> importPovertyAssistance(ValidateList<PovertyAssistance> povertyAssistanceList) {
        int size = povertyAssistanceList.size();
        int i = povertyAssistanceMapper.insertBatch(povertyAssistanceList);
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> insertPovertyAssistance(PovertyAssistance povertyAssistance) {
        int i = povertyAssistanceMapper.insert(povertyAssistance);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updatePovertyAssistance(PovertyAssistance povertyAssistance) {
        boolean update = UpdateChain.of(PovertyAssistance.class)
                .set(PovertyAssistance::getPovertyType, povertyAssistance.getPovertyType(), StringUtils.hasLength(povertyAssistance.getPovertyType()))
                .set(PovertyAssistance::getPovertyAssistanceStandard, povertyAssistance.getPovertyAssistanceStandard(), StringUtils.hasLength(povertyAssistance.getPovertyAssistanceStandard()))
                .set(PovertyAssistance::getPovertyLevel, povertyAssistance.getPovertyLevel(), StringUtils.hasLength(povertyAssistance.getPovertyLevel()))
                .where(PovertyAssistance::getPovertyAssistanceId).eq(povertyAssistance.getPovertyAssistanceId())
                .update();
        if (!update)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deletePovertyAssistance(String povertyAssistanceId) {
        int i = povertyAssistanceMapper.deleteById(povertyAssistanceId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> addStudentPovertyAssistance(StudentPovertyAssistance StudentPovertyAssistance) {
        int i = mapper.insert(StudentPovertyAssistance);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> importStudentPovertyAssistance(ValidateList<StudentPovertyAssistance> studentPovertyAssistanceList) {
        int size = studentPovertyAssistanceList.size();
        int i = mapper.insertBatch(studentPovertyAssistanceList);
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<Page<StudentPovertyAssistanceItem>> getStudentPovertyAssistance(PovertyAssistanceQuery query) {
        CompletableFuture<Page<StudentPovertyAssistanceItem>> future = CompletableFuture.supplyAsync(() -> {
            int pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
            int pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
            return QueryChain.of(StudentPovertyAssistance.class)
                    .select(
                            POVERTY_ASSISTANCE.ALL_COLUMNS,
                            STUDENT_POVERTY_ASSISTANCE.ALL_COLUMNS,
                            STUDENT_BASIC.NAME,
                            GRADE.GRADE_NAME,
                            MAJOR.MAJOR_NAME
                    )
                    .from(STUDENT_POVERTY_ASSISTANCE)
                    .innerJoin(POVERTY_ASSISTANCE).on(POVERTY_ASSISTANCE.POVERTY_ASSISTANCE_ID.eq(STUDENT_POVERTY_ASSISTANCE.POVERTY_ASSISTANCE_ID))
                    .innerJoin(STUDENT_BASIC).on(STUDENT_BASIC.STUDENT_ID.eq(STUDENT_POVERTY_ASSISTANCE.STUDENT_ID))
                    .innerJoin(GRADE).on(GRADE.GRADE_ID.eq(STUDENT_BASIC.GRADE_ID))
                    .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT_BASIC.MAJOR_ID))
                    .where(
                            STUDENT_BASIC.STUDENT_ID.like(query.getSearch())
                                    .or(STUDENT_BASIC.NAME.like(query.getSearch()))
                                    .or(POVERTY_ASSISTANCE.POVERTY_TYPE.like(query.getSearch()))
                                    .or(POVERTY_ASSISTANCE.POVERTY_ASSISTANCE_STANDARD.like(query.getSearch()))
                    )
                    .and(STUDENT_BASIC.GRADE_ID.eq(query.getGradeId()))
                    .and(STUDENT_BASIC.MAJOR_ID.eq(query.getMajorId()))
                    .and(STUDENT_POVERTY_ASSISTANCE.ASSISTANCE_YEAR.eq(query.getAssistanceYear()))
                    .and(POVERTY_ASSISTANCE.POVERTY_LEVEL.eq(query.getPovertyLevel()))
                    .pageAs(Page.of(pageNo, pageSize), StudentPovertyAssistanceItem.class);
        }, readThreadPool);
        Page<StudentPovertyAssistanceItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentPovertyAssistance(StudentPovertyAssistance studentPovertyAssistance) {
        boolean update = UpdateChain.of(StudentPovertyAssistance.class)
                .set(StudentPovertyAssistance::getStudentId, studentPovertyAssistance.getStudentId(), StringUtils.hasLength(studentPovertyAssistance.getStudentId()))
                .set(StudentPovertyAssistance::getPovertyAssistanceId, studentPovertyAssistance.getPovertyAssistanceId(), StringUtils.hasLength(studentPovertyAssistance.getPovertyAssistanceId()))
                .set(StudentPovertyAssistance::getAssistanceYear, studentPovertyAssistance.getAssistanceYear(), StringUtils.hasLength(studentPovertyAssistance.getAssistanceYear()))
                .where(StudentPovertyAssistance::getStudentPovertyAssistanceId).eq(studentPovertyAssistance.getStudentPovertyAssistanceId())
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudentPovertyAssistance(String studentPovertyAssistanceId) {
        int i = mapper.deleteById(studentPovertyAssistanceId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<List<PovertyAssistanceStatGroup>> getStudentPovertyAssistanceStat(PovertyAssistanceStatQuery query) {
        CompletableFuture<List<PovertyAssistanceStatGroup>> future = CompletableFuture.supplyAsync(() -> {
            List<PovertyAssistanceStatRow> rows = povertyAssistanceMapper.getPovertyAssistanceStat(query);
            Map<String, List<PovertyAssistanceStatRow>> gradeMap = rows.stream()
                    .collect(Collectors.groupingBy(PovertyAssistanceStatRow::getGradeName));
            return gradeMap.entrySet().stream()
                    .map(gradeEntry -> {
                        Map<String, List<PovertyAssistanceStatRow>> majorMap = gradeEntry.getValue().stream()
                                .collect(Collectors.groupingBy(PovertyAssistanceStatRow::getMajorName));

                        List<PovertyAssistanceStatGroup.MajorGroup> majorGroups = majorMap.entrySet().stream()
                                .map(majorEntry -> {
                                    Map<String, List<PovertyAssistanceStatRow>> levelMap = majorEntry.getValue().stream()
                                            .collect(Collectors.groupingBy(PovertyAssistanceStatRow::getPovertyLevel));

                                    List<PovertyAssistanceStatGroup.LevelGroup> levelGroups = levelMap.entrySet().stream()
                                            .map(levelEntry -> new PovertyAssistanceStatGroup.LevelGroup(
                                                    levelEntry.getKey(),
                                                    String.valueOf(levelEntry.getValue().size())))
                                            .collect(Collectors.toList());

                                    return new PovertyAssistanceStatGroup.MajorGroup(majorEntry.getKey(), levelGroups);
                                })
                                .collect(Collectors.toList());

                        return new PovertyAssistanceStatGroup(gradeEntry.getKey(), majorGroups);
                    })
                    .collect(Collectors.toList());
        }, readThreadPool);
        List<PovertyAssistanceStatGroup> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }
}
