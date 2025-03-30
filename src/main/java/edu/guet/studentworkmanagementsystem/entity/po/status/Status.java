package edu.guet.studentworkmanagementsystem.entity.po.status;

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

@Table("status")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Status {
    @Id(keyType = KeyType.Auto)
    @NotBlank(message = "学籍id不能为空", groups = {UpdateGroup.class})
    private String statusId;
    @NotBlank(message = "学籍名不能为空", groups = {InsertGroup.class})
    private String statusName;
}
