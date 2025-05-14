package edu.guet.studentworkmanagementsystem.entity.vo.student.archive;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentBase {
    /**
     * 个人信息
     */
    private String studentId;
    private String idNumber;      // 身份证号
    private String name;          // 姓名
    private String gender;        // 性别
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;  // 生日
    private String hobbies;       // 个人兴趣爱好特长
    private String nativePlace;     // 籍贯
    private String nation;        // 民族
    private String height;        // 身高
    private String weight;        // 体重
    private String politicStatus; // 政治面貌
    private String phone;         // 联系手机
    private String email;         // 邮箱
    /**
     * 在校信息
     */
    private String headerTeacherUsername; // 班主任工号
    private String headerTeacherRealName; // 班主任姓名
    private String headerTeacherPhone;    // 班主任联系方式
    private String dormitory;     // 宿舍号
    private String classNo;       // 班级
    private String majorName;     // 专业名
    private String degreeName;    // 培养层次名
    private String gradeName;     // 年级名
    /**
     * 高考信息
     */
    private String studentType;   // 类型
    private String admissionBatch; // 批次
    private String subjectCategory; // 科类
    private String provinceName;  // 省份名称
    private String examId;        // 考生号
    private String admittedMajor; // 录取专业
    private String volunteerMajor; // 投档志愿
    private String volunteerCollege; // 投档单位
    private String totalExamScore;  // 高考总分
    private String convertedScore;  // 总分
    private String specialScore;    // 特征成绩
    private String feature;         // 考生特征
    private String volunteer1;      // 志愿1
    private String volunteer2;      // 志愿2
    private String volunteer3;      // 志愿3
    private String volunteer4;      // 志愿4
    private String volunteer5;      // 志愿5
    private String volunteer6;      // 志愿6
    private String studentFrom;     // 生源地
    private Boolean isAdjusted;     // 调剂
    private String receiver;        // 收件人
    private String receiverPhone;   // 联系电话
    private String postalCode;      // 邮政编码
    private String highSchoolCode;  // 中学代码
    private String highSchoolName;  // 中学名称
    private String candidateCategoryClassification; // 考生类别分类
    private String graduationCategoryClassification; // 毕业类别分类
    private String graduationCategory; // 毕业类别
    private String candidateCategory; // 考生类别
    private String foreignLanguage;  // 高考外语语种
    private String scoreChinese;        // 语文
    private String scoreMath;           // 数学
    private String scoreForeignLanguage; // 外语
    private String scoreComprehensive;  // 综合
    private String scorePhysics;        // 物理
    private String scoreChemistry;      // 化学
    private String scoreBiology;        // 生物
    private String scorePolitics;       // 政治
    private String scoreHistory;        // 历史
    private String scoreGeography;      // 地理
    private String scoreTechnology;     // 技术
    private String selectedSubjects;    // 选考科目
    /**
     * 户口信息
     */
    private String householdRegistration;   // 户籍所在地
    private String householdType;   // 户口类型
    private String address;         // 地址
    private String fatherName; // 父亲姓名
    private String fatherPhone; // 父亲联系方式
    private String fatherOccupation; // 父亲职业
    private String motherName; // 母亲姓名
    private String motherPhone; // 母亲联系方式
    private String motherOccupation; // 母亲职业
    private String guardian; // 监护人姓名
    private String guardianPhone; // 监护人联系方式
    private String familyPopulation; // 家庭人口
    private String familyMembers; // 家庭成员
    private String familyLocation; // 家庭所在地
    private Boolean isOnlyChild; // 是否独生子女
    /**
     * 其他
     */
    private Boolean studentLoans; // 助学贷款
    private Boolean disability; // 残疾
    private String religiousBeliefs; // 宗教信仰
    /**
     * 备注
     */
    private String otherNotes; // 备注
}
