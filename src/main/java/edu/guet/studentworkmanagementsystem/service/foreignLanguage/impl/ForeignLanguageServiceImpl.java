package edu.guet.studentworkmanagementsystem.service.foreignLanguage.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.foreignLanguage.ForeignLanguageQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.foreignLanguage.ForeignLanguageStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.foreignLanguage.ForeignLanguage;
import edu.guet.studentworkmanagementsystem.entity.vo.foreignLanguage.ForeignLanguageItem;
import edu.guet.studentworkmanagementsystem.entity.vo.foreignLanguage.ForeignLanguageStatItem;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.foreignLanguage.ForeignLanguageMapper;
import edu.guet.studentworkmanagementsystem.service.foreignLanguage.ForeignLanguageService;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static edu.guet.studentworkmanagementsystem.entity.po.foreignLanguage.table.ForeignLanguageTableDef.FOREIGN_LANGUAGE;
import static edu.guet.studentworkmanagementsystem.entity.po.foreignLanguage.table.LanguageTableDef.LANGUAGE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.DegreeTableDef.DEGREE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.GradeTableDef.GRADE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentBasicTableDef.STUDENT_BASIC;

@Service
public class ForeignLanguageServiceImpl extends ServiceImpl<ForeignLanguageMapper, ForeignLanguage> implements ForeignLanguageService {
    private final ThreadPoolTaskExecutor readThreadPool;

    public ForeignLanguageServiceImpl(@Qualifier("readThreadPool") ThreadPoolTaskExecutor readThreadPool) {
        this.readThreadPool = readThreadPool;
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> insertForeignLanguageBatch(ValidateList<ForeignLanguage> foreignLanguages) {
        int i = mapper.insertBatch(foreignLanguages);
        int size = foreignLanguages.size();
        if (i != size)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateForeignLanguage(ForeignLanguage foreignLanguage) {
        boolean update = UpdateChain.of(ForeignLanguage.class)
                .set(FOREIGN_LANGUAGE.LANGUAGE_ID, foreignLanguage.getLanguageId(), StringUtils::hasLength)
                .set(FOREIGN_LANGUAGE.SCORE, foreignLanguage.getScore(), StringUtils::hasLength)
                .set(FOREIGN_LANGUAGE.DATE, foreignLanguage.getDate(), StringUtils::hasLength)
                .set(FOREIGN_LANGUAGE.CERTIFICATE, foreignLanguage.getCertificate(), StringUtils::hasLength)
                .where(FOREIGN_LANGUAGE.FOREIGN_LANGUAGE_ID.eq(foreignLanguage.getForeignLanguageId()))
                .update();
        if (!update)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteForeignLanguage(String foreignLanguageId) {
        int i = mapper.deleteById(foreignLanguageId);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    public BaseResponse<Page<ForeignLanguageItem>> getForeignLanguages(ForeignLanguageQuery query) {
        CompletableFuture<Page<ForeignLanguageItem>> future = CompletableFuture.supplyAsync(() -> {
            int pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
            int pageSize = Optional.ofNullable(query.getPageSize()).orElse(10);
            return QueryChain.of(ForeignLanguage.class)
                    .select(
                            FOREIGN_LANGUAGE.ALL_COLUMNS,
                            LANGUAGE.ALL_COLUMNS,
                            STUDENT_BASIC.STUDENT_ID,
                            STUDENT_BASIC.NAME,
                            MAJOR.MAJOR_NAME,
                            GRADE.GRADE_NAME
                    )
                    .from(FOREIGN_LANGUAGE)
                    .innerJoin(LANGUAGE).on(LANGUAGE.LANGUAGE_ID.eq(FOREIGN_LANGUAGE.LANGUAGE_ID))
                    .innerJoin(STUDENT_BASIC).on(STUDENT_BASIC.STUDENT_ID.eq(FOREIGN_LANGUAGE.STUDENT_ID))
                    .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT_BASIC.MAJOR_ID))
                    .innerJoin(GRADE).on(GRADE.GRADE_ID.eq(STUDENT_BASIC.GRADE_ID))
                    .innerJoin(DEGREE).on(DEGREE.DEGREE_ID.eq(STUDENT_BASIC.DEGREE_ID))
                    .where(
                            FOREIGN_LANGUAGE.STUDENT_ID.like(query.getSearch())
                                    .or(STUDENT_BASIC.NAME.like(query.getSearch()))
                                    .or(FOREIGN_LANGUAGE.CERTIFICATE.eq(query.getSearch()))
                    )
                    .and(MAJOR.MAJOR_ID.eq(query.getMajorId()))
                    .and(GRADE.GRADE_ID.eq(query.getGradeId()))
                    .and(DEGREE.DEGREE_ID.eq(query.getDegreeId()))
                    .and(LANGUAGE.LANGUAGE_ID.eq(query.getLanguageId()))
                    .and(FOREIGN_LANGUAGE.DATE.eq(query.getDate()))
                    .and(FOREIGN_LANGUAGE.CERTIFICATE.eq(query.getCertificate()))
                    .pageAs(Page.of(pageNo, pageSize), ForeignLanguageItem.class);
        }, readThreadPool);
        Page<ForeignLanguageItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    public BaseResponse<ForeignLanguageStatItem> getForeignLanguageStat(ForeignLanguageStatQuery query) {
        // todo: tobe finish
        return null;
    }
}
