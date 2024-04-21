package edu.guet.studentworkmanagementsystem.entity.po.cadre;

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
 * 职位表 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "cadre")
public class Cadre implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    @NotBlank(message = "职位id不能为空", groups = {UpdateGroup.class})
    private String cadreId;
    /**
     * 具体职位
     */
    @NotBlank(message = "职位名称不能为空", groups = {InsertGroup.class})
    private String cadrePosition;
    /**
     * 职位级别
     */
    @NotBlank(message = "职位级别不能为空", groups = {InsertGroup.class})
    private String cadreLevel;
}
