package edu.guet.studentworkmanagementsystem.entity.po.scholarship;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

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
    @NotBlank(message = "学生奖学金id不能为空", groups = {UpdateGroup.class})
    private String studentScholarshipId;
    private String studentId;
    @NotBlank(message = "奖学金id不能为空", groups = {InsertGroup.class})
    private String scholarshipId;
    /**
     * 获奖学年, YYYY-YYYY
     */
    @NotBlank(message = "奖学年不能为空", groups = {InsertGroup.class})
    @Pattern(regexp = "^2\\d{3}-2\\d{3}$", message = "请输入正确的获奖学年格式, 例如: 2023-2024", groups = {InsertGroup.class, UpdateGroup.class})
    private String awardYear;
}
