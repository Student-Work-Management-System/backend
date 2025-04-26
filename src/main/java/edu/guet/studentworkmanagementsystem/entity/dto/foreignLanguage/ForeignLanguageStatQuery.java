package edu.guet.studentworkmanagementsystem.entity.dto.foreignLanguage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForeignLanguageStatQuery {
    private String gradeId;
    private String majorId;
    private String languageId;
}
