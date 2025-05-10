package edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PovertyAssistanceStatQuery {
    private String gradeId;
    private String majorId;
}
