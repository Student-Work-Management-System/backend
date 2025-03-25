package edu.guet.studentworkmanagementsystem.entity.vo.foreignLanguage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForeignLanguageItem implements Serializable {
    private String foreignLanguageId;
    private String studentId;
    private String name;
    private String majorName;
    private String gradeName;
    private String languageId;
    private String languageName;
    private String score;
    private String type;
    private String date;
    private String certificate;
}
