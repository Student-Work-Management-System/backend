package edu.guet.studentworkmanagementsystem.entity.po.cet;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;

/**
 * 学生CET成绩记录 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_cet")
public class StudentCet implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    @Column(value = "student_CET_id")
    private Long studentCETId;
    @Id
    private String studentId;
    private Long score;
    /**
     * 考试学期, 如: 2022-2023_1
     */
    private String examDate;
    /**
     * 证书编号
     */
    private String certificateNumber;
    /**
     * 四级:CET4;六级:CET6
     */
    private String examType;
}
