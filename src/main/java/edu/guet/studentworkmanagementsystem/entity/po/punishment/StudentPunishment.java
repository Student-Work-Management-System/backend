package edu.guet.studentworkmanagementsystem.entity.po.punishment;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;

/**
 * 处分信息 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_punishment")
public class StudentPunishment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Long studentPunishmentId;

    @Id
    private String studentId;

    /**
     * 处分级别：警告、严重警告、记过、留校查看、开除学籍
     */
    private String punishmentLevel;

    /**
     * 处分理由
     */
    private String punishmentReason;

    /**
     * 处分日期
     */
    private Date punishmentDate;

}
