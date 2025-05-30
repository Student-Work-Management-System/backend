package edu.guet.studentworkmanagementsystem.entity.vo.foreignLanguage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LanguageStatRow {
    private String gradeName;
    private String majorName;
    private String languageName;
    private String passNumber;
    private String totalNumber;
}
