package edu.guet.studentworkmanagementsystem.entity.po.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.mybatisflex.annotation.Table;
import edu.guet.studentworkmanagementsystem.common.DigitsOnly;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

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
@Table("student")
public class Student implements Serializable {
    @DigitsOnly(message = "学号只能由数字组成")
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
     * 学历层次
     */
    private String degreeId;
    /**
     * 邮政编码
     */
    private String postalCode;
    @NotBlank(message = "手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号只能由11位数字组成")
    @DigitsOnly(message = "手机号只能由11位数字组成")
    private String phone;
    @Email(message = "请输入正确的邮箱")
    private String email;
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
    private String gradeId;
    /**
     * 班号
     */
    private String classNo;
    /**
     * 班主任
     */
    private String headerTeacherUsername;
    /**
     * 政治面貌
     */
    @NotBlank(message = "政治面貌不能为空")
    private String politicId;
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
    private String  foreignScore;
    /**
     * 个人兴趣爱好特长
     */
    private String hobbies;
    /**
     * 宿舍
     */
    private String dormitory;
    /**
     * 入团时间
     */
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joiningTime;
    /**
     * 是否学生贷款
     */
    private Boolean isStudentLoans;
    /**
     * 身高
     */
    private String height;
    /**
     * 体重
     */
    private String weight;
    /**
     * 宗教信仰
     */
    private String religiousBeliefs;
    /**
     * 家庭所在地
     */
    private String location;
    /**
     * 家庭人口
     */
    private String familyPopulation;
    /**
     * 家庭成员
     */
    private String familyMembers;
    /**
     * 独生子女
     */
    private Boolean isOnlyChild;
    /**
     * 是否残疾
     */
    private Boolean disability;
    /**
     * 其他标签备注
     */
    private String otherNotes;
    private Boolean enabled;
}
