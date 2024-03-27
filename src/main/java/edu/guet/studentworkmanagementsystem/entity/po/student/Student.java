package edu.guet.studentworkmanagementsystem.entity.po.student;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
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
    private String studentId;
    /**
     * 学生的身份证号
     */
    @Id
    private String idNumber;
    @Id
    private String name;
    private String gender;
    /**
     * 籍贯
     */
    private String nativePlace;
    /**
     * 邮政编码
     */
    private String postalCode;
    private String phone;
    /**
     * 民族
     */
    private String nation;
    /**
     * 专业
     */
    private String majorIn;
    /**
     * 年级
     */
    private String grade;
    /**
     * 班号
     */
    private String classNo;
    /**
     * 政治面貌
     */
    private String politicsStatus;
}
