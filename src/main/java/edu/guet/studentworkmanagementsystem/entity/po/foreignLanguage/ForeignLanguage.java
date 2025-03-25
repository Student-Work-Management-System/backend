package edu.guet.studentworkmanagementsystem.entity.po.foreignLanguage;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "学生外语成绩记录id不能为空", groups = {UpdateGroup.class})
    private String foreignLanguageId;
    @NotBlank(message = "学号不能为空", groups = {InsertGroup.class})
    private String studentId;
    @NotBlank(message = "外语不能为空", groups = {InsertGroup.class})
    private String languageId;
    @NotBlank(message = "成绩不能为空", groups = {InsertGroup.class})
    private String score;
    @NotBlank(message = "考试类型不能为空", groups = {InsertGroup.class})
    private String examType;
    @NotBlank(message = "考试日期不能为空", groups = {InsertGroup.class})
    private String examDate;
    private String certificate;
}
