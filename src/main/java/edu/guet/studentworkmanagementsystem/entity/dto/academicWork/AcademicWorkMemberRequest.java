package edu.guet.studentworkmanagementsystem.entity.dto.academicWork;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcademicWorkMemberRequest {
    private String memberOrder;
    private String username;
}
