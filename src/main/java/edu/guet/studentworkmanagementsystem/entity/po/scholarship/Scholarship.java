package edu.guet.studentworkmanagementsystem.entity.po.scholarship;

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
 * 奖学金 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "scholarship")
public class Scholarship implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Long scholarshipId;
    /**
     * 奖学金名称
     */
    private String scholarshipName;
    /**
     * 奖学金级别
     */
    private String scholarshipLevel;
}
