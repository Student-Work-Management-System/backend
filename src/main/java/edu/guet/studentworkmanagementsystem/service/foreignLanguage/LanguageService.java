package edu.guet.studentworkmanagementsystem.service.foreignLanguage;

import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.po.foreignLanguage.Language;

import java.util.List;

public interface LanguageService extends IService<Language> {
    <T> BaseResponse<T> addLanguage(Language language);
    <T> BaseResponse<T> updateLanguage(Language language);
    <T> BaseResponse<T> deleteLanguage(String languageId);
    BaseResponse<List<Language>> getLanguages();
}
