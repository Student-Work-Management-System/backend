package edu.guet.studentworkmanagementsystem.entity.dto.competition;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionAuditDTO implements Serializable {
    @NotBlank(message = "学生竞赛id不能为空")
    private String studentCompetitionId;
    private String reviewState;
    private String rejectReason;
    private String auditorId;
}
