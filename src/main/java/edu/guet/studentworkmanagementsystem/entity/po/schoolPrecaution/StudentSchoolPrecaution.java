package edu.guet.studentworkmanagementsystem.entity.po.schoolPrecaution;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    private String studentSchoolPrecautionId;
    @NotBlank(message = "学号不能为空", groups = {InsertGroup.class})
    private String studentId;
    /**
     * 学业预警等级
     */
    @NotBlank(message = "学业预警等级不能为空", groups = {InsertGroup.class})
    private String schoolPrecautionLevel;
    /**
     * YYYY-YYYY_term
     */
    @NotBlank(message = "学业预警学期不能为空", groups = {InsertGroup.class})
    @Pattern(regexp = "^2\\d{3}-2\\d{3}_[1|2]$", message = "请输入正确的学业预警学期格式, 例如: 2023-2024_1")
    private String precautionTerm;
    /**
     * 原因
     */
    @NotBlank(message = "学业预警理由不能为空", groups = {InsertGroup.class})
    private String detailReason;
    /**
     * 备注
     */
    private String comment;
}
