package edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance;

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
 * 学生贫困资助认定相关记录 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_poverty_assistance")
public class StudentPovertyAssistance implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    private Long studentPovertyAssistanceId;
    @Id
    private String studentId;
    @Id
    private Long povertyAssistanceId;
    /**
     * 认证学年, YYYY-YYYY
     */
    private String assistanceYear;
}
