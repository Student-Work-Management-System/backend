package edu.guet.studentworkmanagementsystem.entity.vo.student.archive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForeignLanguageBase {
    private String languageName;
    private String score;
    private String type;
    private String date;
    private String certificate;
}
