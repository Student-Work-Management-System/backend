package edu.guet.studentworkmanagementsystem.entity.po.competition;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 竞赛记录 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "competition")
public class Competition implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    private Long competitionId;
    /**
     * 竞赛名称
     */
    private String competitionName;
    /**
     * 竞赛性质： 团队/单人
     */
    private String competitionNature;
    /**
     * 竞赛级别
     */
    private String competitionLevel;
}
