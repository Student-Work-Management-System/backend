package edu.guet.studentworkmanagementsystem.entity.dto.leave;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveStatQuery {
    private String gradeId;
    private String majorId;
}
