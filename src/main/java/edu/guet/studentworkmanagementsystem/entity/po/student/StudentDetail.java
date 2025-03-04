package edu.guet.studentworkmanagementsystem.entity.po.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(value = "student_detail")
public class StudentDetail implements Serializable {
    /**
     * 学号, 外键关联student表
     */
    @Id
    private String studentId;
    /**
     * 班主任id
     */
    private String headTeacherId;
    /**
     * 专业id, 由专业表进行约束
     */
    private String majorId;
    /**
     * 籍贯
     */
    private String nativePlace;
    /**
     * 邮政编码
     */
    private String postalCode;
    /**
     * 民族
     */
    private String nation;
    /**
     * 政治面貌
     */
    private String politicsStatus;
    /**
     * 年级
     */
    private String grade;
    /**
     * 班号
     */
    private String classNo;
    /**
     * 宿舍
     */
    private String dormitory;
    /**
     * 出生日期
     */
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
    /**
     * 户籍所在地
     */
    private String householdRegistration;
    /**
     * 户口类型
     */
    private String householdType;
    /**
     * 家庭住址
     */
    private String address;
    /**
     * 父亲姓名
     */
    private String fatherName;
    /**
     * 父亲联系方式
     */
    private String fatherPhone;
    /**
     * 父亲职业
     */
    private String fatherOccupation;
    /**
     * 母亲姓名
     */
    private String motherName;
    /**
     * 母亲联系方式
     */
    private String motherPhone;
    /**
     * 母亲职业
     */
    private String motherOccupation;
    /**
     * 监护人姓名
     */
    private String guardian;
    /**
     * 监护人联系方式
     */
    private String guardianPhone;
    /**
     * 中学名称
     */
    private String highSchool;
    /**
     * 考生号
     */
    private String examId;
    /**
     * 录取批次
     */
    private String admissionBatch;
    /**
     * 高考总分
     */
    private String totalExamScore;
    /**
     * 外语语种
     */
    private String foreignLanguage;
    /**
     * 外语分数
     */
    private String foreignScore;
    /**
     * 个人兴趣爱好特长
     */
    private String hobbies;
    /**
     * 其他标签备注
     */
    private String otherNotes;
}
