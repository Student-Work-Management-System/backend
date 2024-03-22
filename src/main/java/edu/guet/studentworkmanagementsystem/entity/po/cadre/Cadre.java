package edu.guet.studentworkmanagementsystem.entity.po.cadre;

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
    private Long cadreId;
    /**
     * 具体职位
     */
    private String cadrePosition;
    /**
     * 职位级别
     */
    private String cadreLevel;
}
