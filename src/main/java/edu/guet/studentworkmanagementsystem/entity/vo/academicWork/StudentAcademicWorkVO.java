package edu.guet.studentworkmanagementsystem.entity.vo.academicWork;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.AcademicWork;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.Author;
import edu.guet.studentworkmanagementsystem.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class StudentAcademicWorkVO implements Serializable {
    private String studentAcademicWorkId;
    private String studentId;
    private String name;
    /**
     * 著作名称
     */
    private String academicWorkName;
    /**
     * 著作类型：论文(1)、专利(2)和软著(3)
     */
    private String academicWorkType;
    private String additionalInfoId;
    /**
     * 学术作品, 根据academic_work_type字段的不同关联不同的表(论文、软著或专利表)
     */
    private AcademicWork academicWork;
    /**
     * 作者顺序,应填入格式：[{ order: 1, studentId:"",authorName:""}...`.]
     */
    private Author[] authors;
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
    public StudentAcademicWorkVO(
            String studentAcademicWorkId,
            String studentId,
            String name,
            String academicWorkName,
            String academicWorkType,
            String additionalInfoId,
            String authors,
            String evidence,
            String auditState,
            String reason
    ) throws JsonProcessingException {
        this.studentAcademicWorkId = studentAcademicWorkId;
        this.studentId = studentId;
        this.name = name;
        this.academicWorkName = academicWorkName;
        this.academicWorkType = academicWorkType;
        this.additionalInfoId = additionalInfoId;
        this.authors = JsonUtil.getMapper().readValue(authors, new TypeReference<>() {});
        this.evidence = evidence;
        this.auditState = auditState;
        this.reason = reason;
    }
    public void setAuthors(String authorsStr) throws JsonProcessingException {
        JsonUtil.getMapper().readValue(authorsStr, new TypeReference<List<Author>>() {});
    }
}
