package edu.guet.studentworkmanagementsystem;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.foreignLanguage.ForeignLanguageStatQuery;
import edu.guet.studentworkmanagementsystem.entity.vo.foreignLanguage.ForeignLanguageStatGrouped;
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
        BaseResponse<List<ForeignLanguageStatGrouped>> foreignLanguageStat = foreignLanguageService.getForeignLanguageStat(query);
        List<ForeignLanguageStatGrouped> data = foreignLanguageStat.getData();
        System.out.println(data);
    }
}
