package edu.guet.studentworkmanagementsystem.entity.po.student;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 学生基础信息 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student")
public class Student implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 学号, 系统中与学生有关的一律使用学号关联
     */
    @Id
    @NotBlank(message = "学号不能为空")
    private String studentId;
    /**
     * 学生的身份证号
     */
    @NotBlank(message = "身份证号不能为空")
    @Size(min = 18, max = 18, message = "请输入正确的身份证号")
    private String idNumber;
    @NotBlank(message = "姓名不能为空")
    private String name;
    @NotBlank(message = "性别不能为空")
    @Pattern(regexp = "^([男女])$")
    private String gender;
    /**
     * 籍贯
     */
    private String nativePlace;
    /**
     * 邮政编码
     */
    private String postalCode;
    @NotBlank(message = "手机号不能为空")
    @Size(min = 11, max = 11, message = "请输入正常的手机号")
    private String phone;
    /**
     * 民族
     */
    private String nation;
    /**
     * 专业
     */
    @NotBlank(message = "专业id不能为空")
    private String majorId;
    /**
     * 年级
     */
    @NotBlank(message = "年级不能为空")
    @Pattern(regexp = "^2\\d{3}$", message = "请输入正确的年级")
    private String grade;
    /**
     * 班号
     */
    private String classNo;
    /**
     * 政治面貌
     */
    @NotBlank(message = "政治面貌不能为空")
    private String politicsStatus;
    private Boolean enabled = true;
}
