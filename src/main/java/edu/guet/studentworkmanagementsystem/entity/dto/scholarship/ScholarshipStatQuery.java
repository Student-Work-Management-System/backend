package edu.guet.studentworkmanagementsystem.entity.dto.scholarship;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScholarshipStatQuery {
    private String gradeId;
    private String majorId;
    private String level;
    private String time;
}
