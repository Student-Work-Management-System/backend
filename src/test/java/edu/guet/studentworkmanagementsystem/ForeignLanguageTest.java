package edu.guet.studentworkmanagementsystem;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.foreignLanguage.ForeignLanguageStatQuery;
import edu.guet.studentworkmanagementsystem.entity.vo.foreignLanguage.ForeignLanguageStatItem;
import edu.guet.studentworkmanagementsystem.service.foreignLanguage.ForeignLanguageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ForeignLanguageTest {
    @Autowired
    private ForeignLanguageService foreignLanguageService;
    @Test
    void getStat() {
        ForeignLanguageStatQuery query = ForeignLanguageStatQuery.builder()
                .build();
        BaseResponse<List<ForeignLanguageStatItem>> foreignLanguageStat = foreignLanguageService.getForeignLanguageStat(query);
        List<ForeignLanguageStatItem> data = foreignLanguageStat.getData();
        long total = 0;
        for (ForeignLanguageStatItem item : data) {
            for (ForeignLanguageStatItem.LanguageStatItem languageStatItem : item.getLanguageStatItems()) {
                long l = Long.parseLong(languageStatItem.getTotalNumber());
                total += l;
            }
        }
        System.out.println(total);
    }
}
