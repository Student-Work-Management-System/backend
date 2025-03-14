package edu.guet.studentworkmanagementsystem.entity.dto.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentQuery implements Serializable {
    // 学号、姓名、身份证、邮箱、手机号、父母姓名、父母手机号、监护人姓名、监护人电话
    private String search;
    // 性别
    private String gender;
    // 籍贯
    private String nativePlace;
    // 学籍id
    private String statusId;
    // 学历层次
    private String degreeId;
    // 专业id
    private String majorId;
    // 年级
    private String gradeId;
    // 民族
    private String nation;
    // 政治面貌
    private String politicsStatus;
    // 班号
    private String classNo;
    // 宿舍
    private String dormitory;
    // 户籍所在地
    private String householdRegistration;
    // 户口类型
    private String householdType;
    // 家庭住址
    private String address;
    // 高中名称
    private String highSchool;
    // 考生号
    private String examId;
    // 录取批次
    private String admissionBatch;
    // 高考总分
    private String totalExamScore;
    // 外语语种
    private String foreignLanguage;
    // 外语分数
    private String foreignScore;
    // 个人兴趣爱好特长
    private String hobbies;
    // 是否学生贷款
    private Boolean isStudentLoans;
    // 宗教信仰
    private String religiousBeliefs;
    // 家庭人口
    private String familyPopulation;
    // 是否独生子女
    private Boolean isOnlyChild;
    // 家庭所在地省/市/县
    private String location;
    //
    private Boolean disability;
    // 其他标签备注
    private String otherNotes;
    private Boolean enabled;
    private Integer pageNo;
    private Integer pageSize;
}

