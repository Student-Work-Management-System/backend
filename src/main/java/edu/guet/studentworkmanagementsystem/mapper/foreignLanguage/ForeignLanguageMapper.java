package edu.guet.studentworkmanagementsystem.mapper.foreignLanguage;

import com.mybatisflex.core.BaseMapper;
import edu.guet.studentworkmanagementsystem.entity.dto.foreignLanguage.ForeignLanguageStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.foreignLanguage.ForeignLanguage;
import edu.guet.studentworkmanagementsystem.entity.vo.foreignLanguage.LanguageStatRow;

import java.util.List;

public interface ForeignLanguageMapper extends BaseMapper<ForeignLanguage> {
    List<LanguageStatRow> getStat(ForeignLanguageStatQuery query);
}
