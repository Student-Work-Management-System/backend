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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("language")
public class Language {
    @Id(keyType = KeyType.Auto)
    @NotBlank(message = "语言id不能为空", groups = {UpdateGroup.class})
    private String languageId;
    @NotBlank(message = "语言不能为空", groups = {InsertGroup.class})
    private String languageName;
    @NotBlank(message = "考试类型不能为空", groups = {InsertGroup.class})
    private String type;
    @NotBlank(message = "考试总分不能为空", groups = {InsertGroup.class})
    private String total;
}
