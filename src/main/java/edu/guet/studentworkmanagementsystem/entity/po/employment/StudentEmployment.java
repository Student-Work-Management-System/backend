package edu.guet.studentworkmanagementsystem.entity.po.employment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 学生就业信息 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_employment")
public class StudentEmployment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    private String studentEmploymentId;
    private String studentId;
    /**
     * 毕业的年份
     */
    private String graduationYear;
    /**
     * 毕业去向
     */
    private String whereabouts;
    /**
     * 单位机构组织代码
     */
    private String code;
    /**
     * 单位名称
     */
    private String organizationName;
    /**
     * 单位性质
     */
    private String jobNature;
    /**
     * 单位所处行业
     */
    private String jobIndustry;
    /**
     * 单位所在地
     */
    private String jobLocation;
    /**
     * 职业类别
     */
    private String category;
    /**
     * 薪水
     */
    private String salary;
    /**
     * 毕业后的状态：就业、待就业、升学、地方/国家基层项目、科研/管理助理
     */
    private String state;
    /**
     * 联系人信息
     */
    private String contactPerson;
    private String contactPhone;
    /**
     * 就业证明日期
     */
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate certificateTime;
    private String comment;
}
