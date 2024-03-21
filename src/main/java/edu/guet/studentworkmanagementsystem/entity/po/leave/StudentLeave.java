package edu.guet.studentworkmanagementsystem.entity.po.leave;

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
 * 请假相关记录 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_leave")
public class StudentLeave implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Long studentLeaveId;

    @Id
    private String studentId;

    /**
     * 请假类型
     */
    private String leaveType;

    /**
     * 请假理由
     */
    private String leaveReason;

    /**
     * 请假日期, YYYY-MM-DD
     */
    private Date leaveDate;

    /**
     * 请假时长, hour
     */
    private Long leaveDuration;

}
