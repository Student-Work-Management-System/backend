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

import java.io.Serial;
import java.io.Serializable;
/**
 * 专业表
 * 2024.3.29创建
 * @author fish
 */
@Table("major")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Major implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    @NotBlank(message = "专业id不能为空", groups = {UpdateGroup.class})
    private String majorId;
    @NotBlank(message = "专业名不能为空", groups = {InsertGroup.class})
    private String majorName;
}
