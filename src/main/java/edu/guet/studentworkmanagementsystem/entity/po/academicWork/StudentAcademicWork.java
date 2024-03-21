package edu.guet.studentworkmanagementsystem.entity.po.academicWork;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;

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
    private Long studentAcademicWorkId;

    @Id
    private String studentId;

    /**
     * 著作名称
     */
    private String academicWorkName;

    /**
     * 著作类型：论文、专利和软著
     */
    private String academicWorkType;

    /**
     * 补充信息, 根据academic_work_type字段的不同关联不同的表(论文、软著或专利表)
     */
    private Long additionalInfoId;

    /**
     * 作者顺序,应填入格式：[{ order: 1, studentId:"",author:""}...`.]
     */
    private String authorsSort;

    /**
     * 证明材料，填写文件地址
     */
    private String evidence;

}
