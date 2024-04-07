package edu.guet.studentworkmanagementsystem.entity.dto.academicWork;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicWorkAuditDTO implements Serializable {
    @NotNull(message = "学术作品id不能为空")
    private Long studentAcademicWorkId;
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
}
