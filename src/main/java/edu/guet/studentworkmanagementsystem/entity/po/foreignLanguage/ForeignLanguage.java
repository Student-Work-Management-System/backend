package edu.guet.studentworkmanagementsystem.entity.po.foreignLanguage;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("foreign_language")
public class ForeignLanguage implements Serializable {
    @Id(keyType = KeyType.Auto)
    private String foreignLanguageId;
    private String studentId;
    private String languageId;
    private String score;
    private String examType;
    private String examDate;
    private String certificate;
}
