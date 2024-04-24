package edu.guet.studentworkmanagementsystem.entity.po.academicWork;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;

/**
 * 著作详细信息表(专利) 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_patent")
public class StudentPatent implements Serializable, AcademicWork {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    private String studentPatentId;
    /**
     * 发表状态：受理、公开或授权
     */
    private String publishState;
    /**
     * 发表日期
     */
    private Date publishDate;
    /**
     * 受理日期
     */
    private Date acceptDate;
    /**
     * 授权日期
     */
    private Date authorizationDate;
}
