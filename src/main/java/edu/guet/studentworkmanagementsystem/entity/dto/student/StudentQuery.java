package edu.guet.studentworkmanagementsystem.entity.dto.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
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
public class StudentQuery implements Serializable {
    // 学号、姓名、身份证、邮箱、手机号、父母姓名、父母手机号、监护人姓名、监护人电话
    private String search;
    // 性别
    private String gender;
    // 籍贯
    private String nativePlace;
    // 专业id
    private String majorId;
    // 年级
    private String grade;
    // 民族
    private String nation;
    // 政治面貌
    private String politicsStatus;
    // 邮政编码
    private String postalCode;
    // 班号
    private String classNo;
    // 宿舍
    private String dormitory;
    // 出生日期
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
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
    // 其他标签备注
    private String otherNotes;
    private Boolean enabled;
    private Integer pageNo;
    private Integer pageSize;
}

