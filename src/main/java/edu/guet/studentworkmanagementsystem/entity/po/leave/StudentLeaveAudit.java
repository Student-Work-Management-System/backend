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
 * 请假批阅 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_leave_audit")
public class StudentLeaveAudit implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    private Long studentLeaveAuditId;
    /**
     * 审批人id
     */
    @Id
    private String auditorId;
    /**
     * 请假记录id
     */
    @Id
    private Long studentLeaveId;
    /**
     * 审批日期
     */
    private Date auditDate;
    /**
     * 审核状态
     * <br/>
     * 审核中, 通过, 拒绝
     */
    private String auditState;
}
