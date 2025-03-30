package edu.guet.studentworkmanagementsystem.entity.dto.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentStatQuery {
    private String degreeId;
    private String gradeId;
}
