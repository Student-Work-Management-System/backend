package edu.guet.studentworkmanagementsystem.entity.po.punishment;

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
@Table("punishment")
public class Punishment {
    @Id(keyType = KeyType.Auto)
    @NotBlank(message = "处分条目id不能为空", groups = { UpdateGroup.class })
    private String punishmentId;
    @NotBlank(message = "处分条目名不能为空", groups = { InsertGroup.class })
    private String punishmentName;
}
