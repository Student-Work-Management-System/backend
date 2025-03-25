package edu.guet.studentworkmanagementsystem.service.foreignLanguage.impl;

import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.po.foreignLanguage.Language;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.foreignLanguage.LanguageMapper;
import edu.guet.studentworkmanagementsystem.service.foreignLanguage.LanguageService;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static edu.guet.studentworkmanagementsystem.entity.po.foreignLanguage.table.LanguageTableDef.LANGUAGE;

@Service
public class LanguageServiceImpl extends ServiceImpl<LanguageMapper, Language> implements LanguageService {
    private final ThreadPoolTaskExecutor readThreadPool;

    public LanguageServiceImpl(@Qualifier("readThreadPool") ThreadPoolTaskExecutor readThreadPool) {
        this.readThreadPool = readThreadPool;
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> addLanguage(Language language) {
        int i = mapper.insert(language);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateLanguage(Language language) {
        boolean update = UpdateChain.of(Language.class)
                .set(LANGUAGE.LANGUAGE_NAME, language.getLanguageName(), StringUtils::hasLength)
                .set(LANGUAGE.TYPE, language.getType(), StringUtils::hasLength)
                .set(LANGUAGE.TOTAL, language.getTotal(), StringUtils::hasLength)
                .where(LANGUAGE.LANGUAGE_ID.eq(language.getLanguageId()))
                .update();
        if (!update)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteLanguage(String languageId) {
        int i = mapper.deleteById(languageId);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    public BaseResponse<List<Language>> getLanguages() {
        CompletableFuture<List<Language>> future = CompletableFuture.supplyAsync(() -> mapper.selectAll(), readThreadPool);
        List<Language> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }
}
