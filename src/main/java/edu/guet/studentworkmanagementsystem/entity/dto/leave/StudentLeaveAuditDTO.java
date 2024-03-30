package edu.guet.studentworkmanagementsystem.entity.dto.leave;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentLeaveAuditDTO implements Serializable {
    @NotBlank(message = "审核人不能为空")
    private String auditorId;
    @NotBlank(message = "待审核记录id不能为空")
    private String studentLeaveId;
    @NotBlank(message = "审核后状态不能为空")
    private String auditState;
}
