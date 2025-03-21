package edu.guet.studentworkmanagementsystem.entity.po.cadre;

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
 * 学生任职记录 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_cadre")
public class StudentCadre implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    @NotBlank(message = "学生任职id不能为空", groups = {UpdateGroup.class})
    private String studentCadreId;
    @NotBlank(message = "学号不能为空", groups = {InsertGroup.class})
    private String studentId;
    @NotBlank(message = "职位id不能为空", groups = {InsertGroup.class})
    private String cadreId;
    @NotBlank(message = "任职开始日期不能为空", groups = {InsertGroup.class})
    private String appointmentStartTerm;
    @NotBlank(message = "任职结束日期不能为空", groups = {InsertGroup.class})
    private String appointmentEndTerm;
    private String comment;
}
