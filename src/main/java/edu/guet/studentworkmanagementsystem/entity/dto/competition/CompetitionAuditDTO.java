package edu.guet.studentworkmanagementsystem.entity.dto.competition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionAuditDTO implements Serializable {
    @NotNull(message = "竞赛id不能为空")
    private String competitionId;
    @NotBlank(message = "学号不能为空")
    private String headerId;
    private String reviewState;
    private String rejectReason;
    private String auditorId;
}
