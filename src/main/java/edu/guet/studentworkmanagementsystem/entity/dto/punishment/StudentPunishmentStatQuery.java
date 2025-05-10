package edu.guet.studentworkmanagementsystem.entity.dto.punishment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentPunishmentStatQuery {
    private String gradeId;
    private String majorId;
}
