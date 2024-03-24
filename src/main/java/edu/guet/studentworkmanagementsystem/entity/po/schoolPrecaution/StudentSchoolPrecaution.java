package edu.guet.studentworkmanagementsystem.entity.po.schoolPrecaution;

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
 * 学业预警信息记录 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_school_precaution")
public class StudentSchoolPrecaution implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    private Long studentSchoolPrecautionId;
    @Id
    private String studentId;
    /**
     * 学业预警等级
     */
    private String schoolPrecautionLevel;
    /**
     * YYYY-YYYY_term
     */
    private String precautionTerm;
    /**
     * 原因
     */
    private String detailReason;
    /**
     * 备注
     */
    private String comment;
}
