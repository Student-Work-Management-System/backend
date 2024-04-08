package edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance;

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
 * 贫困资助记录 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "poverty_assistance")
public class PovertyAssistance implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    @NotBlank(message = "贫困认定id不能为空", groups = {UpdateGroup.class})
    private String povertyAssistanceId;
    /**
     * 贫困认定等级, 特别/比较/一般困难
     */
    @NotBlank(message = "贫困等级不能为空", groups = {InsertGroup.class})
    private String povertyLevel;
    /**
     * 贫困类型
     */
    @NotBlank(message = "贫困类型不能为空", groups = {InsertGroup.class})
    private String povertyType;
    /**
     * 资助标准
     */
    @NotBlank(message = "贫困补助标准不能为空", groups = {InsertGroup.class})
    private String povertyAssistanceStandard;
}
