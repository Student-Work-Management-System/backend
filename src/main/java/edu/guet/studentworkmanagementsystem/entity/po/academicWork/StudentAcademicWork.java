package edu.guet.studentworkmanagementsystem.entity.po.academicWork;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.StudentAcademicWorkDTO;
import edu.guet.studentworkmanagementsystem.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 学生作品相关(论文、专利和软著) 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_academic_work")
public class StudentAcademicWork implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    private String studentAcademicWorkId;
    @Id
    private String studentId;
    /**
     * 著作名称
     */
    private String academicWorkName;
    /**
     * 著作类型：论文(1)、专利(2)和软著(3)
     */
    private String academicWorkType;
    /**
     * 补充信息, 根据academic_work_type字段的不同关联不同的表(论文、软著或专利表)
     */
    private String additionalInfoId;
    /**
     * 作者顺序,应填入格式：[{ order: 1, studentId:"",authorName:""}...`.]
     */
    private String authors;
    /**
     * 证明材料，填写文件地址
     */
    private String evidence;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate uploadTime;
    /**
     * 上报后审核状态
     */
    private String auditState;
    /**
     * 审核人id
     */
    private String auditorId;
    /**
     * 拒绝理由
     */
    private String reason;
    public StudentAcademicWork(StudentAcademicWorkDTO studentAcademicWorkDTO) throws JsonProcessingException {
        this.studentId = studentAcademicWorkDTO.getStudentId();
        this.academicWorkName = studentAcademicWorkDTO.getAcademicWorkName();
        this.academicWorkType = studentAcademicWorkDTO.getAcademicWorkType();
        this.additionalInfoId = studentAcademicWorkDTO.getAdditionalInfoId();
        this.authors = JsonUtil.mapper.writeValueAsString(studentAcademicWorkDTO.getAuthors());
        this.evidence = studentAcademicWorkDTO.getEvidence();
        this.uploadTime = LocalDate.now();
    }
}
