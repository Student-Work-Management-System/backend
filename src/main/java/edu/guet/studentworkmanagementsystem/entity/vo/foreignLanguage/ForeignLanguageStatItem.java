package edu.guet.studentworkmanagementsystem.entity.vo.foreignLanguage;

import lombok.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForeignLanguageStatItem {
    private String majorName;
    private List<LanguageStatItem> languageStatItems;
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LanguageStatItem {
        private String languageName;
        private String passNumber;
        private String totalNumber;
    }
}
