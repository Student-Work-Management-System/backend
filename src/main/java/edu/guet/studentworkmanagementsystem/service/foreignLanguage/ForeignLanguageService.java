package edu.guet.studentworkmanagementsystem.service.foreignLanguage;

import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.foreignLanguage.ForeignLanguageQuery;
import edu.guet.studentworkmanagementsystem.entity.po.foreignLanguage.ForeignLanguage;
import edu.guet.studentworkmanagementsystem.entity.vo.foreignLanguage.ForeignLanguageItem;

import java.util.List;
import java.util.Set;

public interface ForeignLanguageService extends IService<ForeignLanguage> {
    <T> BaseResponse<T> insertForeignLanguageBatch(ValidateList<ForeignLanguage> foreignLanguages);
    <T> BaseResponse<T> updateForeignLanguage(ForeignLanguage foreignLanguage);
    <T> BaseResponse<T> deleteForeignLanguage(String foreignLanguageId);
    BaseResponse<List<ForeignLanguageItem>> getForeignLanguages(ForeignLanguageQuery query);
    BaseResponse<Set<String>> getOptionExamType();
    BaseResponse<Set<String>> getOptionExamDate();
}
