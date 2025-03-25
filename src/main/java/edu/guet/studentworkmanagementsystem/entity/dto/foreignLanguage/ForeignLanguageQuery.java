package edu.guet.studentworkmanagementsystem.entity.dto.foreignLanguage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForeignLanguageQuery implements Serializable {
    private String search;
    private String majorId;
    private String gradeId;
    private String degreeId;
    private String languageId;
    private String examType;
    private String examDate;
    private String certificate;
    private Integer pageNo;
    private Integer pageSize;
}
