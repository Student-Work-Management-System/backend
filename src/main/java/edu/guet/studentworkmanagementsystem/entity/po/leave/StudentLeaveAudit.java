package edu.guet.studentworkmanagementsystem.entity.po.leave;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.StudentLeaveAuditDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

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
    private String studentLeaveId;
    /**
     * 审批日期
     */
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate auditDate;
    /**
     * 审核状态
     * <br/>
     * 审核中, 通过, 拒绝
     */
    private String auditState = "审核中";
    public StudentLeaveAudit(StudentLeaveAuditDTO dto) {
        this.auditorId = dto.getAuditorId();
        this.auditDate = dto.getAuditDate();
        this.studentLeaveId = dto.getStudentLeaveId();
        this.auditState = dto.getAuditState();
    }
}
