package edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "学生贫困记录id不能为空", groups = {UpdateGroup.class})
    private String studentPovertyAssistanceId;
    @NotBlank(message = "学号不能为空", groups = {InsertGroup.class})
    private String studentId;
    @NotBlank(message = "贫困标准(povertyAssistanceId)不能为空", groups = {InsertGroup.class})
    private String povertyAssistanceId;
    /**
     * 认证学年, YYYY-YYYY
     */
    @NotBlank(message = "认证学年不能为空", groups = {InsertGroup.class})
    private String assistanceYear;
}
