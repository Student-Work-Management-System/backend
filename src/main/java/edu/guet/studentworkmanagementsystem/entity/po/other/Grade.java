package edu.guet.studentworkmanagementsystem.entity.po.other;

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

@Table("grade")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Grade {
    @Id(keyType = KeyType.Auto)
    @NotBlank(message = "年级id不能为空", groups = {UpdateGroup.class})
    private String gradeId;
    @NotBlank(message = "年级名不能为空", groups = {InsertGroup.class})
    private String gradeName;
}
