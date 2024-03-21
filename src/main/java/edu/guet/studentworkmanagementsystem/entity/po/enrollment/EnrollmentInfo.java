package edu.guet.studentworkmanagementsystem.entity.po.enrollment;

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
 * 招生信息 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "enrollment_info")
public class EnrollmentInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Long enrollmentInfoId;

    private String examId;

    private String idNumber;

    /**
     * 考生姓名
     */
    private String name;

    /**
     * 生源地
     */
    private String from;

    /**
     * 录取学院
     */
    private String enrollCollege;

    /**
     * 录取专业
     */
    private String enrollMajor;

    /**
     * 第一志愿专业
     */
    private String firstMajor;

    /**
     * 高考总分
     */
    private Long score;

}
