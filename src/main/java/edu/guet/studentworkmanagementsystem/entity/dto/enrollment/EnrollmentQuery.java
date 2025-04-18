package edu.guet.studentworkmanagementsystem.entity.dto.enrollment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentQuery implements Serializable {
    private String search; // 个人信息搜索框: 学号、姓名、身份证、邮箱、手机号
    private String familySearch; // 家庭信息搜索框: 父母姓名、父母手机号、监护人姓名、监护人电话
    private String householdRegistration; // 户籍所在地
    private String householdType; // 户口类型
    private Boolean isOnlyChild; // 独生子女
    private String schoolSearch; // 在校信息搜索框: 宿舍号、班号
    private String headerTeacherSearch; // 班主任信息搜索框: 班主任工号、姓名、联系方式
    private String highSchoolSearch; // 高中信息搜索框: 中学名称、中学代码
    private String candidateCategory; // 考生类别
    private String studentType; // 学生类型
    private String admissionBatch; // 录取批次
    private String politicId; // 政治面貌
    private String gender; // 性别
    private String majorId; // 专业
    private String gradeId; // 年级
    private String degreeId; // 培养层次
    private String statusId; // 学籍状态
    private Boolean studentLoans; // 助学贷款
    private Boolean disability; // 是否残疾
    private String religiousBeliefs; // 宗教信仰
    private Boolean enabled;
    private Integer pageNo;
    private Integer pageSize;
}
