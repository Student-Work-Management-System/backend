package edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;

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
    private Long povertyAssistanceId;

    /**
     * 贫困认定等级, 特别/比较/一般困难
     */
    private String povertyLevel;

    /**
     * 贫困类型
     */
    private String povertyType;

    /**
     * 资助标准
     */
    private String povertyAssistanceStandard;

}
