package edu.guet.studentworkmanagementsystem.entity.dto.academicWork;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcademicWorkMember {
    private String memberOrder;
    private String uid;
    private Boolean isStudent;
}
