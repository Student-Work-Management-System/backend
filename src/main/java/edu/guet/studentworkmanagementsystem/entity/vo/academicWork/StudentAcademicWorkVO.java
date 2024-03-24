package edu.guet.studentworkmanagementsystem.entity.vo.academicWork;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.AcademicWork;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.Authors;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.StudentAcademicWork;
import edu.guet.studentworkmanagementsystem.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAcademicWorkVO implements Serializable {
    private Long studentAcademicWorkId;
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
     * 学术作品, 根据academic_work_type字段的不同关联不同的表(论文、软著或专利表)
     */
    private AcademicWork academicWork;
    /**
     * 作者顺序,应填入格式：[{ order: 1, studentId:"",authorName:""}...`.]
     */
    private Authors authors;
    /**
     * 证明材料，填写文件地址
     */
    private String evidence;
    /**
     * 上报后审核状态
     */
    private String auditState;
    /**
     * 拒绝理由
     */
    private String reason;
    public StudentAcademicWorkVO(StudentAcademicWork studentAcademicWork, AcademicWork academicWork) throws JsonProcessingException {
        this.studentAcademicWorkId = studentAcademicWork.getStudentAcademicWorkId();
        this.studentId = studentAcademicWork.getStudentId();
        this.academicWorkName = studentAcademicWork.getAcademicWorkName();
        this.academicWorkType = studentAcademicWork.getAcademicWorkType();
        this.academicWork = academicWork;
        this.authors = JsonUtil.mapper.readValue(studentAcademicWork.getAuthors(), Authors.class);
        this.evidence = studentAcademicWork.getEvidence();
        this.auditState = studentAcademicWork.getAuditState();
        this.reason = studentAcademicWork.getReason();
    }
}
