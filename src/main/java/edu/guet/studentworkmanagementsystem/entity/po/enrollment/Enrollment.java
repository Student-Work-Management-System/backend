package edu.guet.studentworkmanagementsystem.entity.po.enrollment;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

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
@Table(value = "enrollment")
public class Enrollment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    @NotBlank(message = "招新信息id不能为空", groups = {UpdateGroup.class})
    private String enrollmentId;
    @NotBlank(message = "准考证号不能为空", groups = {InsertGroup.class})
    private String examineeId;
    @NotBlank(message = "身份证号不能为空", groups = {InsertGroup.class})
    private String id;
    /**
     * 考生姓名
     */
    @NotBlank(message = "学生姓名不能为空", groups = {InsertGroup.class})
    private String name;
    /**
     * 生源地
     */
    @NotBlank(message = "生源地不能为空", groups = {InsertGroup.class})
    private String origin;
    /**
     * 录取学院
     */
    @NotBlank(message = "录取学院不能为空", groups = {InsertGroup.class})
    private String enrollSchool;
    /**
     * 录取专业
     */
    @NotBlank(message = "录取专业不能为空", groups = {InsertGroup.class})
    private String enrollMajor;
    /**
     * 第一志愿专业
     */
    @NotBlank(message = "第一志愿专业不能为空", groups = {InsertGroup.class})
    private String firstMajor;
    /**
     * 高考总分
     */
    @NotNull(message = "高考分数不能为空", groups = {InsertGroup.class})
    @Column("NCEE_score")
    private Long NCEEScore;
}
