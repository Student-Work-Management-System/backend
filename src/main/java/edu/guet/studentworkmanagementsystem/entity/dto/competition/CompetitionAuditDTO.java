package edu.guet.studentworkmanagementsystem.entity.dto.competition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^(已通过|已拒绝)$")
    private String reviewState;
    private String rejectReason;
}
