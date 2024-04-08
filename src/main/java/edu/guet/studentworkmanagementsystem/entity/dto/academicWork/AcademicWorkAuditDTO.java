package edu.guet.studentworkmanagementsystem.entity.dto.academicWork;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicWorkAuditDTO implements Serializable {
    @NotBlank(message = "学术作品id不能为空")
    private String studentAcademicWorkId;
    /**
     * 上报后审核状态
     */
    @Pattern(regexp = "^(已通过|已拒绝)$")
    private String auditState;
    /**
     * 审核人id
     */
    @NotBlank(message = "审核人id不能为空")
    private String auditorId;
    /**
     * 拒绝理由
     */
    private String reason;
}
