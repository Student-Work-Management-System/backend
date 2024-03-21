package edu.guet.studentworkmanagementsystem.entity.po.status;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;

/**
 * 学籍 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_status")
public class StudentStatus implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Long studentStatusId;

    @Id
    private String studentId;

    /**
     * 学籍状态, 只有在校和离校这两种大类
     */
    private String state;

    /**
     * 学籍处理
     */
    private String handle;

    /**
     * 变动日期
     */
    private Date changedDate;

}
