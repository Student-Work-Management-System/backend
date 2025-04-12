package edu.guet.studentworkmanagementsystem.entity.po.enrollment;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
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
    @Id(keyType = KeyType.Auto)
    private String enrollmentId;  // 主键

    private String studentId;     // 学号
    private String idNumber;      // 身份证号
    private String majorId;       // 专业
    private String degreeId;      // 培养层次
    private String classNo;       // 班级
    private String name;          // 姓名
    private String gender;        // 性别
    private String nation;        // 民族
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
    private String isAdjusted;      // 调剂
    private String volunteer1;      // 志愿1
    private String volunteer2;      // 志愿2
    private String volunteer3;      // 志愿3
    private String volunteer4;      // 志愿4
    private String volunteer5;      // 志愿5
    private String volunteer6;      // 志愿6
    private String receiver;        // 收件人
    private String receiverPhone;   // 联系电话
    private String postalCode;      // 邮政编码
    private String address;         // 地址
    private String highSchoolCode;  // 中学代码
    private String highSchoolName;  // 中学名称
    private String politicId;      // 政治面貌
    private String candidateCategoryClassification; // 考生类别分类
    private String graduationCategoryClassification; // 毕业类别分类
    private String graduationCategory; // 毕业类别
    private String candidateCategory; // 考生类别
    private String foreignLanguageId;  // 外语语种
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
    private String phone;               // 联系手机
}
