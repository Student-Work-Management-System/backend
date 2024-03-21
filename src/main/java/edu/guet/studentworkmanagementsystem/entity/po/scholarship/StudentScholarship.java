package edu.guet.studentworkmanagementsystem.entity.po.scholarship;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;

/**
 * 奖学金相关记录 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_scholarship")
public class StudentScholarship implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Long studentScholarshipId;

    @Id
    private String studentId;

    @Id
    private Long scholarshipId;

    /**
     * 获奖学年, YYYY-YYYY
     */
    private String awardYear;

}
