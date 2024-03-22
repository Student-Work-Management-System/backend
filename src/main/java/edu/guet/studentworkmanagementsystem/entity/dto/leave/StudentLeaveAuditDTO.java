package edu.guet.studentworkmanagementsystem.entity.dto.leave;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentLeaveAuditDTO implements Serializable {
    @NotBlank(message = "审核人不能为空")
    private String auditorId;
    @NotNull(message = "待审核记录id不能为空")
    private Long studentLeaveId;
    private Date auditDate;
    private String auditState;
}
