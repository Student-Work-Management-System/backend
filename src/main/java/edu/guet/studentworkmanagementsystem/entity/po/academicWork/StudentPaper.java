package edu.guet.studentworkmanagementsystem.entity.po.academicWork;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;

/**
 * 著作详细信息表(论文) 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_paper")
public class StudentPaper implements Serializable, AcademicWork {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    private String studentPaperId;
    /**
     * 期刊名称
     */
    private String periodicalName;
    /**
     * 期刊JRC分区
     */
    private String jrcPartition;
    /**
     * 期刊中科院分区
     */
    private String casPartition;
    /**
     * 录稿时间
     */
    private Date recordedTime;
    /**
     * 检索时间
     */
    private Date searchedTime;
    /**
     * 是否会议文章
     */
    private Boolean isMeeting;
    /**
     * 是否中文核心文章
     */
    private Boolean isChineseCore;
    /**
     * 是否EI文章
     */
    @Column(value = "is_EI")
    private Boolean isEI;
    /**
     * 是否EI收录
     */
    @Column(value = "is_EI_recorded")
    private Boolean isEIRecorded;
}
